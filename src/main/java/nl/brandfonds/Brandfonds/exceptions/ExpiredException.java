package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredException extends BaseException {
    protected ExpiredException(String format, Object... args) {
        super(String.format(format, args), HttpStatus.UNAUTHORIZED);
    }

    public static class LinkExpiredException extends ExpiredException {
        public LinkExpiredException(String link) {
            super("The provided link '%s' is expired", link);
        }
    }
}
