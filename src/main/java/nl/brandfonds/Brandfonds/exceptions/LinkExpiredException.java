package nl.brandfonds.Brandfonds.exceptions;

public class LinkExpiredException extends Exception {

    public LinkExpiredException(){

    }

    public LinkExpiredException(String message){
        super(message);
    }
}
