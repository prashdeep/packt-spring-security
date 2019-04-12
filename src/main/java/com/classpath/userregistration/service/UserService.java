package com.classpath.userregistration.service;

import com.classpath.userregistration.model.User;
import com.classpath.userregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmailAddress(String emailAddress){
        return this.userRepository.findByEmail(emailAddress);
    }

    public void saveUser(User user){
        String password = user.getPassword();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user2 = new User();
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setEmail(user.getEmail());
        user2.setPassword(encryptedPassword);
        user2.setConfirmPassword(encryptedPassword);
        this.userRepository.save(user2);
    }
}