package com.classpath.userregistration.model;

import com.classpath.userregistration.constraint.PasswordMatch;
import lombok.Data;

@PasswordMatch(message = "Password and confirm password field should match",password = "password")
@Data
public class UserPassword {

    private String password;

    private String confirmPassword;
}