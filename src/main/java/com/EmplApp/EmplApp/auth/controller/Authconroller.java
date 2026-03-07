package com.EmplApp.EmplApp.auth.controller;


import com.EmplApp.EmplApp.auth.jwt.JwtService;
import com.EmplApp.EmplApp.auth.model.User;
import com.EmplApp.EmplApp.auth.service.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class Authconroller {

    @Autowired
    RegisterUser registerUser;

    @Autowired
    JwtService jwt;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public User register(@RequestBody User user){

       return registerUser.register(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody  User user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated() ){



            return jwt.generateToken(user);
        }
        else{
            return "false";
        }

    }


}
