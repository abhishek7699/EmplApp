//package com.EmplApp.EmplApp.service;
//
//import com.EmplApp.EmplApp.exception.EmployeeExistsException;
//import com.EmplApp.EmplApp.exception.EmployeeNotFoundException;
//import com.EmplApp.EmplApp.model.EmplRecords;
//import com.EmplApp.EmplApp.model.dto.EmployeeCreateDTO;
//import com.EmplApp.EmplApp.repo.EmplRepo;
//import com.EmplApp.EmplApp.utilty.ObjectMapping;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//
//import java.text.ParseException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class EmployeeServiceTest {
//
//    @Mock
//    private EmplRepo repo;
//
//    @InjectMocks
//    private EmplService service;
//
//    private EmployeeCreateDTO validDto;
//    private EmplRecords existingEmp;
//
//    ObjectMapping map= new ObjectMapping();
//
//
//    @BeforeEach
//    void setUp() {
//        validDto = new EmployeeCreateDTO(
//                "John Doe",
//                "john@example.com",
//                "Engineering",
//                "Senior Engineer",
//                123456789L,
//                LocalDate.of(1990, 5, 15)
//        );
//
//        existingEmp = new EmplRecords();
//        existingEmp.setEmail("john@example.com");
//        existingEmp.setName("John Doe");
//        existingEmp.setEmpId(UUID.randomUUID().toString());
//    }
//
//    @Test
//    @DisplayName("viewEmployee -> returns null when not found (matching current service logic)")
//    void viewEmployee_notFound() {
//        // Your service currently returns repo.FindByID(email) directly without throwing
//        when(repo.FindByID("unknown@example.com")).thenReturn(null);
//
//        EmplRecords result = service.viewEmployee("unknown@example.com");
//
//        assertThat(result).isNull();
//    }
//
//    @Test
//    @DisplayName("allEmployee -> returns sorted list when data exists")
//    void allEmployee_success() {
//        EmplRecords emp2 = new EmplRecords();
//        emp2.setName("Alice");
//
//        List<EmplRecords> list = new ArrayList<>(List.of(existingEmp, emp2));
//        when(repo.FindAll()).thenReturn(list);
//
//        Page<EmplRecords> result = service.allEmployee(0,5);
//
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getName()).isEqualTo("Alice"); // Verification of sorting
//    }
//
//    @Test
//    @DisplayName("allEmployee -> empty list -> throws exception")
//    void allEmployee_emptyList_throws() {
//        when(repo.FindAll()).thenReturn(List.of());
//
//        assertThatThrownBy(() -> service.allEmployee(0,5))
//                .isInstanceOf(EmployeeNotFoundException.class)
//                .hasMessage("list is Empty");
//    }
//
//    @Test
//    @DisplayName("addEmployee -> new email -> calls AddRecord and returns success")
//    void addEmployee_success() throws ParseException {
//        when(repo.FindByID(validDto.email())).thenReturn(null);
//
//        EmplRecords entity = map.convertToEntity(validDto);
//        String result = service.addEmployee(entity);
//
//        assertThat(result).contains("added to the list");
//        // Verify the custom native query was called with correct params
//        verify(repo, times(1)).AddRecord(
//                eq(entity.getEmail()),
//                anyString(), // UUID is generated inside
//                eq(entity.getName()),
//                eq(entity.getDob()),
//                eq(entity.getSsn()),
//                eq(entity.getDept()),
//                eq(entity.getDesignation())
//        );
//    }
//
//    @Test
//    @DisplayName("addEmployee -> duplicate email -> throws EmployeeExistsException")
//    void addEmployee_duplicate_throws() {
//        when(repo.FindByID("john@example.com")).thenReturn(existingEmp);
//
//        EmplRecords entity = map.convertToEntity(validDto);
//
//        assertThatThrownBy(() -> service.addEmployee(entity))
//                .isInstanceOf(EmployeeExistsException.class)
//                .hasMessageContaining("Already Exists");
//
//        verify(repo, never()).AddRecord(any(), any(), any(), any(), any(), any(), any());
//    }
//
//    @Test
//    @DisplayName("EditEmployee -> exists -> calls EditRecord")
//    void editEmployee_exists() throws ParseException {
//        when(repo.FindByID("john@example.com")).thenReturn(existingEmp);
//
//        EmplRecords updateData = map.convertToEntity(validDto);
//        updateData.setEmpId("fixed-id");
//
//        String result = service.EditEmployee(updateData);
//
//        assertThat(result).contains("Updated");
//        verify(repo).EditRecord(any(), any(), any(), any(), any(), eq("john@example.com"), any());
//    }
//
//    @Test
//    @DisplayName("DeleteEmployee -> exists -> calls DeleteRecord")
//    void deleteEmployee_exists() throws ParseException {
//        when(repo.FindByID("john@example.com")).thenReturn(existingEmp);
//
//        String result = service.DeleteEmployee("john@example.com");
//
//        assertThat(result).contains("deleted");
//        verify(repo).DeleteRecord("john@example.com");
//    }
//
//    @Test
//    @DisplayName("DeleteEmployee -> not found -> throws exception")
//    void deleteEmployee_notFound() {
//        when(repo.FindByID("missing@example.com")).thenReturn(null);
//
//        assertThatThrownBy(() -> service.DeleteEmployee("missing@example.com"))
//                .isInstanceOf(EmployeeNotFoundException.class)
//                .hasMessageContaining("does not exist cannot delete");
//    }
//}