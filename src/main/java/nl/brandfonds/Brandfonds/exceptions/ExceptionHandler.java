package nl.brandfonds.Brandfonds.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({BaseException.class})
    public ResponseEntity<Object> handleAnyException(BaseException ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage(), ex.getClass().getSimpleName());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), ex.getHttpStatus());
    }
}
