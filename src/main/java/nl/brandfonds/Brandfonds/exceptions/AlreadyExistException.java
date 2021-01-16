package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistException extends Exception {

    public AlreadyExistException() {

    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
