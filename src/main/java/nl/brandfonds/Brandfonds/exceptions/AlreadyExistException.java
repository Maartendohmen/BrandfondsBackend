package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistException extends BaseException {

    public AlreadyExistException(String format, Object... args) {
        super(String.format(format, args), HttpStatus.CONFLICT);
    }

    public static class FileAlreadyExistException extends AlreadyExistException {
        public FileAlreadyExistException(String fileName) {
            super("File '%s' already exists on storage", fileName);
        }
    }

    public static class UserAlreadyExistException extends AlreadyExistException {
        public UserAlreadyExistException(String email) {
            super("User with email '%s' already exists", email);
        }
    }
}
