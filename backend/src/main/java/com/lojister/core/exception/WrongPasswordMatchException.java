package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongPasswordMatchException extends BaseException {

    public WrongPasswordMatchException(String message) {
        super(message);
    }

    public WrongPasswordMatchException(String message, String enMessage) {
        super(message, enMessage);
    }
}
