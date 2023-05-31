package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidDateTimeException extends BaseException {

    public InvalidDateTimeException(String message) {
        super(message);
    }

    public InvalidDateTimeException(String message, String enMessage) {
        super(message, enMessage);
    }

}
