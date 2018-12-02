package us.filin.api.uploader.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Customer Config Not Found")  // 404
public class CustomerConfigNotFoundException extends RuntimeException {
    public CustomerConfigNotFoundException(String message) {
        super(message);
    }

    public CustomerConfigNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}