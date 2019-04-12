package com.classpath.userregistration.constraint;

import com.classpath.userregistration.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, User> {

    private String message;
    private String password;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
            this.password = constraintAnnotation.password();
            this.message = constraintAnnotation.message();

    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        String enteredPassword = user.getPassword();
        String enteredConfirmPassword = user.getConfirmPassword();
        boolean flag = enteredPassword.equals(enteredConfirmPassword);

        if (!flag){
            context.buildConstraintViolationWithTemplate(this.message)
                    .addPropertyNode(this.password)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return flag;
    }
}