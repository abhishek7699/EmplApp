package com.EmplApp.EmplApp.auth.service;

import com.EmplApp.EmplApp.auth.model.User;
import com.EmplApp.EmplApp.auth.repo.UserPrincipal;
import com.EmplApp.EmplApp.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {


    @Autowired
    UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user= repo.findByUsername(username);

        if(user==null){
            throw new UsernameNotFoundException("User  not found 404");

        }
        return new UserPrincipal(user);
    }
}
