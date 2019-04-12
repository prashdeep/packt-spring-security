package com.classpath.userregistration.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String message() default "Password and Confirm Password fields should match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String password();
}