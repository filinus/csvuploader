package us.filin.api.uploader.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad Input Data")  // 400
public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
    public BadInputException(String message, Throwable t) {
        super(message, t);
    }
}