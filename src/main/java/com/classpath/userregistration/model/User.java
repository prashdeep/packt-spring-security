package com.classpath.userregistration.model;

import com.classpath.userregistration.constraint.PasswordMatch;
import com.classpath.userregistration.constraint.ValidPassword;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "User")
@Data
@PasswordMatch(password = "password",
        message = "password and confirm password fields should match")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "first name cannot be empty")
    private String firstName;

    @NotEmpty(message = "last name cannot be empty")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Please enter a valid email address")
    @NotEmpty(message = "email cannot be empty")
    private String email;

    @ValidPassword(message = "Invalid Password.")
    private String password;

    @Transient
    @NotEmpty(message = "confirm password cannot be blank")
    private String confirmPassword;


    @NotEmpty(message = "confirm password cannot be blank")
    private String confirmationToken;

}