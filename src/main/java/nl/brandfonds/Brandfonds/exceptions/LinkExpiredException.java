package nl.brandfonds.Brandfonds.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@NoArgsConstructor
public class LinkExpiredException extends Exception {
    public LinkExpiredException(String message) {
        super(message);
    }
}
