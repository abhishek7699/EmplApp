package com.EmplApp.EmplApp.service;

import com.EmplApp.EmplApp.config.RabbitMQConfig;
import com.EmplApp.EmplApp.dto.RabbitMQDTO;
import com.EmplApp.EmplApp.exception.EmployeeExistsException;
import com.EmplApp.EmplApp.exception.EmployeeNotFoundException;
import com.EmplApp.EmplApp.model.EmplRecords;
import com.EmplApp.EmplApp.repo.command.EmplRepoCommand;
import com.EmplApp.EmplApp.repo.query.EmplRepoQuery;
import com.EmplApp.EmplApp.utilty.RabbitMqConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Service
public class EmplServiceCommand implements EmplInterfaceCommand{

    @Autowired
    private EmplRepoCommand repo;


    @Autowired
    private ObjectMapper objectMapper;

            @Autowired
            private RabbitTemplate rabbitTemplate;

            RabbitMqConverter rQ= new RabbitMqConverter();






    String error="employee with email not found";



    @Override
    public String addEmployee(EmplRecords emp) throws ParseException {

        emp.setEmpId(UUID.randomUUID().toString());

        if ((repo.FindByID(emp.getEmail()))==null) {
            repo.AddRecord(emp.getEmail(),emp.getEmpId(),emp.getName(),emp.getDob(),emp.getSsn(),emp.getDept(),emp.getDesignation());


            EmplRecords emp_queue=repo.FindByID(emp.getEmail());
            RabbitMQDTO rq= rQ.converter(emp_queue);
            rq.setAction("add");

            String message = objectMapper.writeValueAsString(rq);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,     // which exchange
                    RabbitMQConfig.ROUTING_KEY,  // which routing key
                    message                       // the message (JSON string)
            );
            System.out.println("Order event published to RabbitMQ!");




            return "An employee was added to the list";
        }
       throw new EmployeeExistsException("The employee Already Exists with this email");
    }

    @Override
    public String EditEmployee(EmplRecords er) throws ParseException {

        EmplRecords employee= repo.FindByID( er.getEmail());
        if (!(employee==null)) {
            String name =er.getName();
            LocalDate dob=er.getDob();
            Long ssn=er.getSsn();
            String dept=er.getDept();
            String designation=er.getDesignation();
            String email=er.getEmail();
            String uuid= employee.getEmpId();

            repo.EditRecord(name,dob,ssn,dept,designation,email,uuid);

            //EmplRecords emp_queue=repo.FindByID(er.getEmail());
            RabbitMQDTO rq= rQ.converter(er);
            rq.setAction("edit");

            String message = objectMapper.writeValueAsString(rq);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,     // which exchange
                    RabbitMQConfig.ROUTING_KEY,  // which routing key
                    message                       // the message (JSON string)
            );
            return "the Employee Record Has been Updated";
        }
        throw new EmployeeNotFoundException("The employee with that email does not exist to edit ");
    }

    @Override
    public String DeleteEmployee(EmplRecords emp) throws ParseException {

        String email= emp.getEmail();

        EmplRecords employee = repo.FindByID(email);

        System.out.println("this line is executed"+employee.getEmail());

       if (!(employee==null)) {

           repo.DeleteRecord(email);


           RabbitMQDTO rq= rQ.converter(employee);
           rq.setAction("delete");

           String message = objectMapper.writeValueAsString(rq);

           rabbitTemplate.convertAndSend(
                   RabbitMQConfig.EXCHANGE,     // which exchange
                   RabbitMQConfig.ROUTING_KEY,  // which routing key
                   message                       // the message (JSON string)
           );
           return "the Employee Record Has been deleted";
        }
        throw new EmployeeNotFoundException("The employee with that email does not exist cannot delete ");
    }


}