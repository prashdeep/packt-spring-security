package com.classpath.userregistration.constraint;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class PasswordConstraintsValidator implements ConstraintValidator<ValidPassword, String> {

    private String message;
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        LengthRule lengthRule = new LengthRule(6,Integer.MAX_VALUE);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 1);
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 1);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 1);
        CharacterRule specialCharRule = new CharacterRule(EnglishCharacterData.Special, 1);

        List<Rule> passwordsRuleList = new ArrayList();
        passwordsRuleList.add(lengthRule);
        passwordsRuleList.add(upperCaseRule);
        passwordsRuleList.add(lowerCaseRule);
        passwordsRuleList.add(digitRule);
        passwordsRuleList.add(specialCharRule);
        passwordsRuleList.add(new WhitespaceRule());

        PasswordValidator validator = new PasswordValidator(passwordsRuleList);
        RuleResult ruleResult = validator.validate(new PasswordData(password));
        boolean flag = ruleResult.isValid();

        if(!flag){
            context.buildConstraintViolationWithTemplate(this.message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return flag;
    }
}