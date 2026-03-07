package com.EmplApp.EmplApp.controller;

import com.EmplApp.EmplApp.model.EmplRecords;
import com.EmplApp.EmplApp.dto.EmployeeCreateDTO;
import com.EmplApp.EmplApp.service.EmplInterfaceCommand;

import com.EmplApp.EmplApp.utilty.ObjectMapping;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("employees-command")
public class EmpControllerCommand {

    @Autowired
    private EmplInterfaceCommand service;


     ObjectMapping map= new ObjectMapping();

    String error="Please Enter a Proper Input";


    @GetMapping({"/","/home"})
    public String Home(){
        return "Hello";
    }




    @PostMapping("new-record")
    @PreAuthorize(("hasRole('ADMIN')"))
    public String addEmployyes(@Valid  @RequestBody EmployeeCreateDTO input1) throws ParseException {
        EmplRecords input= map.convertToEntity(input1);

        if (input == null || input.getEmail() == null) {

            return error;
        }

        return service.addEmployee(input);
    }


    @DeleteMapping("remove-record")
    @PreAuthorize(("hasRole('ADMIN')"))
    @CacheEvict(value="emplrecords", key="#input.email")
    public String deleteEmployee(@RequestBody EmplRecords input) throws ParseException {

        if (input == null || input.getEmail() == null) {

           return error;
        }


        return service.DeleteEmployee(input);
    }

    @PatchMapping("change-record")
    @PreAuthorize(("hasRole('ADMIN')"))
    @CacheEvict(value="emplrecords", key="#input.email")
    public String editEmployee(@Valid @RequestBody EmployeeCreateDTO input1) throws ParseException {
        EmplRecords input= map.convertToEntity(input1);
        if (input == null || input.getEmail() == null) {

            return error;

        }


        return service.EditEmployee(input);
    }
}
