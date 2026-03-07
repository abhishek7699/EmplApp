package com.EmplApp.EmplApp.auth.repo;

import com.EmplApp.EmplApp.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {

    User findByUsername(String username);

}
