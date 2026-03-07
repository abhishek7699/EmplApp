package com.EmplApp.EmplApp.auth.service;


import com.EmplApp.EmplApp.auth.model.User;
import com.EmplApp.EmplApp.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUser {

    @Autowired
    UserRepo repo;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);

    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);

    }

}
