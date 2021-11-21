package nl.brandfonds.Brandfonds.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@NoArgsConstructor
public class UserDisabledException extends Exception {
    public UserDisabledException(String message) {
        super(message);
    }
}
