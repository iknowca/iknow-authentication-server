package xyz.iknow.authenticaionserver.utility.validator;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {
    public boolean validate(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
