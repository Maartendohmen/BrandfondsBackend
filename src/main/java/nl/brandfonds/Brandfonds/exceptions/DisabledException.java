package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DisabledException extends BaseException {

    protected DisabledException(String format, Object... args) {
        super(String.format(format, args), HttpStatus.FORBIDDEN);
    }

    public static class UserDisabledException extends DisabledException {
        public UserDisabledException(String identifier, String value) {
            super("User with %s '%s' is currently disabled");
        }
    }
}
