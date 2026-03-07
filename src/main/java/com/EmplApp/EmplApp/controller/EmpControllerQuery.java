package com.EmplApp.EmplApp.controller;

import com.EmplApp.EmplApp.model.EmplRecords;
import com.EmplApp.EmplApp.service.EmplInterfaceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employees-query")
public class EmpControllerQuery {

    @Autowired
    private EmplInterfaceQuery service;






    @GetMapping({"/","/home"})
    public String Home(){
        return "Hello";
    }





    @GetMapping("list/redis")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Cacheable(value="emplrecords")
    public List<EmplRecords> listAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        System.out.println("Data from db");

        Page<EmplRecords> pageObj = service.allEmployee(page,size);
        List<EmplRecords> list= pageObj.getContent();
        return list;


    }


    @GetMapping("record")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Cacheable(value="emplrecords",key="#input.email")
    public EmplRecords listEmp(@RequestBody EmplRecords input){
        System.out.println("Data from db");


        if (input == null || input.getEmail() == null) {

            return null;
        }

        String userEmail = input.getEmail();

        return service.viewEmployee(userEmail);
    }


}
