package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LinkExpiredException extends Exception {

    public LinkExpiredException() {

    }

    public LinkExpiredException(String message) {
        super(message);
    }
}
