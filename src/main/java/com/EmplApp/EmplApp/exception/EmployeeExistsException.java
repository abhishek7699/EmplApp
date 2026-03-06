package com.EmplApp.EmplApp.exception;

public class EmployeeExistsException extends RuntimeException{


    public EmployeeExistsException(String message){
        super(message);
    }
}
