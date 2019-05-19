package com.classpath.userregistration.model;

import com.classpath.userregistration.constraint.PasswordMatch;
import com.classpath.userregistration.constraint.ValidPassword;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

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

    @ValidPassword(message = "Password should contain atleast one uppercase" +
            "letter, one lowercase letter, one digit and one special" +
            "character")
    private String password;

    @Transient
    @NotEmpty(message = "confirm password cannot be blank")
    private String confirmPassword;

    @Column(name="token")
    private String token;

    @Column(name = "token_created_time")
    private LocalDateTime tokenCreatedTime;

    @Column(name="enabled")
    private boolean enabled;

}