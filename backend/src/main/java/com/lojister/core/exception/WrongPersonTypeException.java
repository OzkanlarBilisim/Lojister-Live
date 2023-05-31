package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongPersonTypeException extends BaseException {

    public WrongPersonTypeException(String message) {
        super(message);
    }

    public WrongPersonTypeException(String message, String enMessage) {
        super(message, enMessage);
    }
}
