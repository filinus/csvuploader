package us.filin.api.uploader.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Fail To Read CSVConfig")  // 500
public class BadCsvConfigException extends RuntimeException {
    public BadCsvConfigException(String message) {
        super(message);
    }

    public BadCsvConfigException(String message, Throwable t) {
        super(message, t);
    }
}