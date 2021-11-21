package nl.brandfonds.Brandfonds.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@NoArgsConstructor
public class AlreadyExistException extends Exception {
    public AlreadyExistException(String message) {
        super(message);
    }
}
