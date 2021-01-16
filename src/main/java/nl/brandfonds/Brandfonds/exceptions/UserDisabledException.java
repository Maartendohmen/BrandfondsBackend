package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserDisabledException extends Exception {

    public UserDisabledException() {

    }

    public UserDisabledException(String message) {
        super(message);
    }
}
