package nl.brandfonds.Brandfonds.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message)
    {
        super(message);
    }
}
