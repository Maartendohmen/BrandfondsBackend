package nl.brandfonds.Brandfonds.exceptions;

public class UserDisabledException extends Exception {

    public UserDisabledException(){

    }

    public UserDisabledException(String message){
        super(message);
    }
}
