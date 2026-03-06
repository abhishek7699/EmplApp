package com.EmplApp.EmplApp.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmplRecords implements Serializable {

    @Id
    private String email;

    private String empId;
    private  String name;

    private LocalDate dob;
    private Long ssn;
    private String dept;
    private String designation;
}
