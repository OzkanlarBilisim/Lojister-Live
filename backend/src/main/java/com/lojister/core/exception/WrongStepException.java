package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongStepException extends BaseException {
    public WrongStepException(String message) {
        super(message);
    }
    public WrongStepException(String message, String enMessage) {
        super(message, enMessage);
    }

}
