package com.example.BloodDonationProject.util.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.BloodDonationProject.util.BloodGroupValidator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

/**
 * Validation annotation for blood group values.
 * Ensures blood group is one of the valid types: O-, O+, A-, A+, B-, B+, AB-, AB+
 */
@Constraint(validatedBy = ValidBloodGroupValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBloodGroup {
    String message() default "Invalid blood group. Must be one of: O-, O+, A-, A+, B-, B+, AB-, AB+";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validator implementation for ValidBloodGroup annotation.
 */
class ValidBloodGroupValidator implements ConstraintValidator<ValidBloodGroup, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return BloodGroupValidator.isValidBloodGroup(value);
    }
}
