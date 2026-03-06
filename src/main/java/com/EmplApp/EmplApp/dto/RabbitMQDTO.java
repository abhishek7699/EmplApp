package com.EmplApp.EmplApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RabbitMQDTO {
    private String email;

    private String empId;
    private  String name;

    private LocalDate dob;
    private Long ssn;
    private String dept;
    private String designation;
    private String action;
}
