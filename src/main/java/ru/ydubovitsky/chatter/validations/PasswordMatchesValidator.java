package ru.ydubovitsky.chatter.validations;

import ru.ydubovitsky.chatter.annotations.PasswordMatches;
import ru.ydubovitsky.chatter.payload.request.SingUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SingUpRequest singUpRequest = (SingUpRequest) o;
        return singUpRequest.getPassword().equals(singUpRequest.getConfirmPassword());
    }
}
