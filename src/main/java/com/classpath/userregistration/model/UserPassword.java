package com.classpath.userregistration.model;

import com.classpath.userregistration.constraint.PasswordMatch;
import com.classpath.userregistration.constraint.ValidPassword;
import lombok.Data;

@PasswordMatch(message = "Password and confirm password fields should match", password = "password")
@Data
public class UserPassword {

    @ValidPassword
    private String password;

    private String confirmPassword;
}
