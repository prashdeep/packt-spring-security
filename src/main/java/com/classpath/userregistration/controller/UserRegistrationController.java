package com.classpath.userregistration.controller;

import com.classpath.userregistration.model.User;
import com.classpath.userregistration.model.UserPassword;
import com.classpath.userregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserRegistrationController {

    private UserService userService;

    @Autowired
    public UserRegistrationController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/register")
    public ModelAndView displayRegistrationPage(ModelAndView modelAndView,
                                                User user){
        modelAndView.addObject(user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(ModelAndView modelAndView,
                                      @Valid User user,
                                     BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            modelAndView.setViewName("register");
            return modelAndView;
        }

        Optional<User> returnedUser = this.userService.
                findByEmailAddress(user.getEmail());
        if(returnedUser.isPresent()){
            modelAndView.addObject("userExists",
                    "User already exists");
            modelAndView.setViewName("register");
            return modelAndView;
        }

        String password = user.getPassword();

        userService.saveUser(user);
        modelAndView.addObject("confirmMessage",
                "User is successfully registered");
        modelAndView.setViewName("registration-success");
        return modelAndView;
    }

    @PostMapping("/passwordReset/{token}")
    public ModelAndView passwordReset(ModelAndView modelAndView,
                                      @PathVariable("token") String confirmationToken,
                                      @Valid UserPassword userPassword,
                                      BindingResult bindingResult){

        try {
            this.userService.resetPassword(userPassword, confirmationToken);
        } catch (IllegalArgumentException exception){

        }

        modelAndView.setViewName("register");
        return modelAndView;
    }


}