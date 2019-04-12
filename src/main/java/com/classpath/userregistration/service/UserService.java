package com.classpath.userregistration.service;

import com.classpath.userregistration.model.User;
import com.classpath.userregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmailAddress(String emailAddress){
        return this.userRepository.findByEmail(emailAddress);
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }
}