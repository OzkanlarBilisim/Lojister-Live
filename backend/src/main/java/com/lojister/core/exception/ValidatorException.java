package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatorException extends BaseException {

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, String enMessage) {
        super(message, enMessage);
    }
}
