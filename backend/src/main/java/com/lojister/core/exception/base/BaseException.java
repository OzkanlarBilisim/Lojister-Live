package com.lojister.core.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;


public class BaseException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;


    private String enMessage;

    public String getEnMessage() {

        if (this.enMessage == null) {
            return this.getMessage();
        }
        return enMessage;
    }

    public BaseException(String message, HttpStatus httpStatus, String enMessage) {
        super(message);
        this.httpStatus = httpStatus;
        this.enMessage = enMessage;
    }

    public BaseException() {
        super();
        httpStatus = (HttpStatus.BAD_REQUEST);
    }

    public BaseException(String message) {
        super(message);
        httpStatus = (HttpStatus.BAD_REQUEST);
    }

    public BaseException(String message, String enMessage) {
        super(message);
        this.enMessage = enMessage;
        httpStatus = (HttpStatus.BAD_REQUEST);
    }

}
