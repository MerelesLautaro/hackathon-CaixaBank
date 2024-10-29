package com.hackathon.bankingapp.Security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Password cannot be empty")
                    .addConstraintViolation();
            return false;
        }

        boolean valid = true;

        if (password.length() < 8) {
            context.buildConstraintViolationWithTemplate("Password must be at least 8 characters long")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.length() > 128) {
            context.buildConstraintViolationWithTemplate("Password must be less than 128 characters long")
                    .addConstraintViolation();
            valid = false;
        }

        if (!password.matches(".*[A-Z].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter")
                    .addConstraintViolation();
            valid = false;
        }

        if (!password.matches(".*\\d.*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one digit")
                    .addConstraintViolation();
            valid = false;
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one special character")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.contains(" ")) {
            context.buildConstraintViolationWithTemplate("Password cannot contain whitespace")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
