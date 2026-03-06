package com.EmplApp.EmplApp.service;

import com.EmplApp.EmplApp.model.EmplRecords;
import org.springframework.data.domain.Page;

import java.text.ParseException;


public interface EmplInterfaceCommand {


    public String addEmployee(EmplRecords e) throws ParseException;
    public String EditEmployee(EmplRecords e) throws ParseException;
    public String DeleteEmployee(EmplRecords e) throws ParseException;


}
