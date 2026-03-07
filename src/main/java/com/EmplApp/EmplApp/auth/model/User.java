package com.EmplApp.EmplApp.auth.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true,nullable=false)
    private String username;

    private String password;

    private String role;

}
