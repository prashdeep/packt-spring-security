package com.classpath.userregistration.service;

import com.classpath.userregistration.model.User;
import com.classpath.userregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<User> findByConfirmationToken(String confirmationToken){
        return this.userRepository.findByToken(confirmationToken);
    }

    public void saveUser(User user){
        String encryptedPassword = this.bCryptPasswordEncoder
                .encode(user.getPassword());

        user.setPassword(encryptedPassword);
        user.setConfirmPassword(encryptedPassword);

        String confirmationToken = UUID.randomUUID().toString();
        LocalDateTime currentTime = LocalDateTime.now();

        user.setToken(confirmationToken);
        user.setTokenCreatedTime(currentTime);
        this.userRepository.save(user);
    }
}