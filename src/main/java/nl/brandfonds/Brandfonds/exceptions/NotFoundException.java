package nl.brandfonds.Brandfonds.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {
    protected NotFoundException(String format, Object... args) {
        super(String.format(format, args), HttpStatus.NOT_FOUND);
    }

    public static class UserNotFoundException extends NotFoundException {

        public UserNotFoundException(Integer id) {
            super("User with id '%s' could not be found", id);
        }

        public UserNotFoundException(String email) {
            super("User with email '%s' could not be found", email);
        }

        public UserNotFoundException(String email, String password) {
            super("User with email '%s' and password '%s' could not be found", email, password);
        }
    }

    public static class FileNotFoundException extends NotFoundException {
        public FileNotFoundException(String fileName, String filePath) {
            super("File with name '%s' could not be found on path '%s'", fileName, filePath);
        }
    }

    public static class DepositRequestNotFoundException extends NotFoundException {
        public DepositRequestNotFoundException(Integer id) {
            super("Deposit request with id %s could not be found", id);
        }
    }

    public static class StockNotFoundException extends NotFoundException {
        public StockNotFoundException() {
            super("Currently is the stock unavailable for retrieving");
        }
    }
}
