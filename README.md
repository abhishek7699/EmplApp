



Employee App
Technical Documentation
Spring Boot 4.0.2  |  CQRS  |  JWT Auth  |  Docker
Version 2.0








1. System Architecture
The application follows a CQRS (Command Query Responsibility Segregation) architecture with an event-driven synchronisation layer and JWT-based authentication, ensuring complete separation between read and write operations.

1.1 Architecture Layers
Layer	Component	Responsibility
Web	EmployeeCommandController	Handles write HTTP requests (POST, PATCH, DELETE)
Web	EmployeeQueryController	Handles read HTTP requests (GET)
Web	AuthController	Handles /auth/register and /auth/login
Service	EmplServiceCommand	Write business logic, UUID generation, RabbitMQ publishing
Service	EmplServiceQuery	Read business logic, RabbitMQ listening, Redis caching
Service	UserService / RegisterUser	User registration, password encoding, JWT issuance
Data	EmplRepoCommand	Native SQL write operations against Command DB
Data	EmplRepoQuery	Native SQL read operations against Query DB
Data	UserRepo	Auth DB — stores users, roles and JWT tokens
Messaging	RabbitMQ	Event broker syncing Command DB to Query DB
Cache	Redis	Caches read results to reduce Query DB hits
Security	JwtFilter	Validates JWT on every request before controller executes

1.2 Infrastructure Services
Service	Technology	Port
Command Database	PostgreSQL 16 (Docker)	5434
Query Database	PostgreSQL 16 (Docker)	5435
Auth Database	PostgreSQL 16 (Docker)	5436
Message Broker	RabbitMQ 3 (Docker)	5672
Cache	Redis (Docker)	6379
DB UI	pgAdmin 4 (Docker)	5050
RabbitMQ UI	RabbitMQ Management	15672


2. Authentication & Security
All endpoints (except /auth/register and /auth/login) are protected by JWT authentication and role-based authorisation via Spring Security.

2.1 Auth Endpoints
Method	Endpoint	Description	Auth Required
POST	/auth/register	Register a new user with username, password and role	No
POST	/auth/login	Login and receive a JWT token	No

2.2 Role-Based Access Control
Role	Permitted Operations
ADMIN	POST /employees, PATCH /employees/{email}, DELETE /employees/{email}, GET /employees, GET /employees/{email}
USER	GET /employees, GET /employees/{email}

2.3 JWT Flow
The authentication flow works as follows:
•	Client sends credentials to POST /auth/login
•	Server validates credentials, issues a signed JWT token
•	Client includes the token in the Authorization header: Bearer <token>
•	JwtFilter intercepts every request, validates the token and sets the SecurityContext
•	Spring Security checks the role and allows or rejects the request

2.4 Security Configuration
Setting	Value
Session Policy	STATELESS — no server-side session
CSRF	Disabled — stateless JWT does not require CSRF protection
Password Encoding	BCryptPasswordEncoder
JWT Secret	Configured via jwt.secret in application.properties
JWT Filter Position	Before UsernamePasswordAuthenticationFilter
OAuth2 Login	Google OAuth2 (optional social login)


3. API Endpoints
All endpoints are versioned under /api/v1 following REST naming conventions — URLs use lowercase plural nouns, identifiers are path variables, and HTTP verbs express the operation.

Base URL: http://localhost:8081/api/v1

3.1 Command Endpoints (Write) — ADMIN only
Method	Endpoint	Description	Request Body
POST	/employees	Creates a new employee with auto-generated UUID	EmployeeCreateDTO
PATCH	/employees/{email}	Updates an existing employee's fields	EmployeeCreateDTO
DELETE	/employees/{email}	Removes an employee record	None

3.2 Query Endpoints (Read) — ADMIN and USER
Method	Endpoint	Description	Params
GET	/employees	Retrieves all employees (paginated)	page, size
GET	/employees/{email}	Retrieves a specific employee by email	None

3.3 REST Design Principles Applied
•	URLs are lowercase plural nouns — no verbs in the URL
•	HTTP method expresses the operation (POST = create, PATCH = partial update, DELETE = remove, GET = read)
•	Identifiers passed as @PathVariable not @RequestBody for DELETE and GET
•	All endpoints return ResponseEntity with proper HTTP status codes
•	APIs versioned under /api/v1 for forward compatibility

3.4 HTTP Status Codes
Operation	Status Code	Meaning
POST /employees	201 Created	Employee successfully created
GET /employees	200 OK	List returned successfully
GET /employees/{email}	200 OK / 404 Not Found	Found or not found
PATCH /employees/{email}	200 OK	Update applied successfully
DELETE /employees/{email}	204 No Content	Deleted, no body returned
Validation failure	400 Bad Request	Invalid input fields
Duplicate employee	409 Conflict	Email already exists
Unauthorised	401 Unauthorized	Missing or invalid JWT
Forbidden	403 Forbidden	Valid JWT but insufficient role


4. CQRS Event Flow
When a write operation occurs the following synchronisation flow is triggered:

User Request
    → Command Controller (validates JWT + ADMIN role)
    → Command Service saves to Command DB
    → Command Service publishes RabbitMQDTO to RabbitMQ
    → Query Service @RabbitListener receives event
    → Query Service checks action type (add / edit / delete)
    → Query Service performs matching operation on Query DB
    → Redis cache is evicted
    → Next GET request fetches fresh data from Query DB

4.1 RabbitMQ Configuration
Component	Name
Queue	emp.queue
Exchange	emp.exchange
Routing Key	emp.routing.key

4.2 RabbitMQDTO Fields
Field	Type	Description
email	String	Employee identifier
empId	String	UUID auto-generated on creation
name	String	Employee full name
dob	LocalDate	Date of birth
ssn	Long	Social security number
dept	String	Department
designation	String	Job title
action	String	"add", "edit", or "delete"


5. Pagination
All list endpoints support pagination via query parameters:

GET /api/v1/employees?page=0&size=10

Parameter	Default	Description
page	0	Page number (zero-based)
size	10	Number of records per page

Pagination is implemented using Spring Data JPA's Pageable with native SQL and a separate count query for total pages calculation. Invalid pagination parameters (page < 0 or size <= 0) are rejected by the PaginationValidationAspect before reaching the controller.


6. Caching (Redis)
Read operations are cached using Redis to reduce database load.

Annotation	Location	Purpose
@EnableCaching	RedisConfig	Enables Spring caching globally
@Cacheable	GET service methods	Returns cached data if available; hits DB on miss
@CacheEvict	receiveMessage method	Clears cache when data changes via RabbitMQ

Cache TTL is set to 10 minutes. The cache is automatically invalidated whenever Command DB changes are synced to the Query DB via RabbitMQ, ensuring read responses always reflect the latest data.


7. AOP (Aspect Oriented Programming)
Cross-cutting concerns are handled via AOP aspects to keep controllers and services focused on business logic.

Aspect	Advice Type	Purpose
PaginationValidationAspect	@Around	Validates page >= 0 and size > 0 before controller executes. Throws IllegalArgumentException if invalid.
PerformanceMonitorAspect	@Around	Logs execution time of service layer methods for performance monitoring.


8. Data Models & Validation
8.1 EmployeeCreateDTO
Field	Validation Rule
name	Not blank, 2–50 characters
email	Must be a valid email format
dept	Not blank, 2–50 characters
designation	Not blank, 2–50 characters
ssn	Exactly 9 digits (100,000,000 to 999,999,999)
dob	Must be a date in the past

8.2 EmplRecords (Entity)
Field	Type	Description
email	String	Primary key
empId	String	UUID auto-generated on creation
name	String	Employee full name
dob	LocalDate	Date of birth
ssn	Long	Social security number
dept	String	Department name
designation	String	Job title

8.3 User (Auth Entity)
Field	Type	Description
id	Long	Primary key, auto-generated
username	String	Unique username — enforced by @Column(unique = true)
password	String	BCrypt-hashed password
role	String	ROLE_ADMIN or ROLE_USER


9. Exception Handling
Exception	HTTP Status	Message
EmployeeNotFoundException	404 Not Found	"Employee not found"
EmployeeExistsException	409 Conflict	"Employee already exists with this email"
MethodArgumentNotValidException	400 Bad Request	Field-level validation errors
IllegalArgumentException	400 Bad Request	Invalid pagination parameters
Exception (General)	500 Internal Server Error	"Unexpected error"


10. Database Interaction (Native Queries)
All queries use native SQL via @Query(nativeQuery = true). All write operations are marked with @Modifying and @Transactional to ensure data consistency and rollback support.

10.1 Three-Database Setup
Database	Container	Port	Purpose
empl_db_command	postgres-command	5434	Employee write data (Command DB)
empl_db_query	postgres-query	5435	Employee read data (Query DB)
empl_db_auth	postgres-Auth	5436	Users, roles and JWT tokens (Auth DB)

10.2 Native SQL Queries
-- Insert
INSERT INTO emplrecords (email, emp_id, name, dob, ssn, dept, designation)
VALUES (:email, :empId, :name, :dob, :ssn, :dept, :designation)

-- Update
UPDATE emplrecords
SET name=:name, dob=:dob, ssn=:ssn, dept=:dept, designation=:designation, emp_id=:empId
WHERE email=:email

-- Delete
DELETE FROM emplrecords WHERE email=:email

-- Paginated Select
SELECT * FROM emplrecords


11. Business Logic Highlights
Feature	Description
UUID Generation	When addEmployee is called, UUID.randomUUID().toString() is automatically generated and assigned to empId
Pagination	Implemented at repository level using Spring Pageable with native SQL and a separate count query
Event Publishing	After every write, Command Service converts entity to RabbitMQDTO, sets action type, serialises to JSON and publishes to RabbitMQ
Event Consuming	Query Service listens to emp.queue via @RabbitListener, deserialises JSON, checks action and performs matching DB operation
Cache Invalidation	Every RabbitMQ event processed by Query Service triggers @CacheEvict ensuring next GET always returns fresh data
Stale Data Prevention	Edit operation uses original request object directly instead of re-fetching from DB to avoid JPA first-level cache returning stale data
JWT Secret	Loaded from jwt.secret in application.properties via @Value to prevent key regeneration on restart
Auth DataSource	Each database (command, query, auth) has its own DataSourceConfig with @Value-injected URLs, supporting Docker profile overrides


12. Infrastructure & Docker
All infrastructure is managed via Docker Compose with healthchecks ensuring the Spring Boot app only starts after all dependent services are healthy.

12.1 Docker Compose Commands
Command	Description
docker-compose up -d	Start all containers in background
docker-compose up --build	Rebuild app image and start all containers
docker-compose down	Stop containers, data is preserved
docker-compose down -v	Stop containers and delete all volumes
docker ps	Verify all containers are running

12.2 Docker Profile
The app uses Spring profiles to switch datasource URLs between local and Docker environments:
•	Local development uses application.properties with localhost and mapped ports
•	Docker deployment uses application-docker.properties with Docker service names as hostnames
•	Profile is activated via SPRING_PROFILES_ACTIVE: docker in docker-compose.yml

12.3 Rebuild Workflow
Any change to .properties files requires a full rebuild before Docker will pick up the change:

./mvnw clean package -DskipTests
docker-compose down
docker-compose up --build

12.4 Healthcheck Configuration
Service	Healthcheck Command
postgres-command	pg_isready -U admin_1 -d empl_db_command
postgres-query	pg_isready -U admin_2 -d empl_db_query
postgres-Auth	pg_isready -U admin_3 -d empl_db_auth
rabbitmq	rabbitmq-diagnostics ping
redis	redis-cli ping
empl-app	depends_on all services with condition: service_healthy


13. Technology Stack
Technology	Version	Purpose
Spring Boot	4.0.2	Application framework
Spring Security	Latest	Authentication, authorisation and JWT filter chain
Spring OAuth2 Client	Latest	Google OAuth2 social login
PostgreSQL	16	Command, Query and Auth databases
RabbitMQ	3	Message broker for CQRS event sync
Redis	Latest	Caching layer
Docker	Latest	Container orchestration
Hibernate	7.2.1	ORM
HikariCP	7.0.2	Connection pooling
Lombok	Latest	Boilerplate reduction
pgAdmin	4	Database UI
Java	21	Runtime (Temurin 21)

<img width="468" height="628" alt="image" src="https://github.com/user-attachments/assets/dc41c22a-cf43-4377-bb28-469850517c34" />
