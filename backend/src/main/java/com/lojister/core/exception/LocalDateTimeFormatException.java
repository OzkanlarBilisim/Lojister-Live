package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalDateTimeFormatException extends BaseException {

    public LocalDateTimeFormatException(String message) {
        super(message);
    }

    public LocalDateTimeFormatException(String message, String enMessage) {
        super(message, enMessage);
    }

}
