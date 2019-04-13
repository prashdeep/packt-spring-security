package com.classpath.userregistration.constraint;

import org.passay.*;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;

public class PasswordConstraintsValidator implements ConstraintValidator<ValidPassword, String> {

    private String message;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        LengthRule lengthRule = new LengthRule(6, Integer.MAX_VALUE);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
        CharacterRule specialCharRule = new CharacterRule(EnglishCharacterData.Special);

        List<Rule> passwordRulesList = new ArrayList<>();
        passwordRulesList.add(lengthRule);
        passwordRulesList.add(upperCaseRule);
        passwordRulesList.add(lowerCaseRule);
        passwordRulesList.add(digitRule);
        passwordRulesList.add(specialCharRule);
        passwordRulesList.add(new WhitespaceRule());

        PasswordValidator validator = new PasswordValidator(passwordRulesList);
        RuleResult ruleResult = validator.validate(new PasswordData(password));
        boolean flag = ruleResult.isValid();

        if (!flag){
            context.buildConstraintViolationWithTemplate(this.message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return flag;
    }
}