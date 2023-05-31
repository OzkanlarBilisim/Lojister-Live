package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmptyStringException extends BaseException {

    public EmptyStringException(String message) {
        super(message);
    }

    public EmptyStringException(String message, String enMessage) {
        super(message, enMessage);
    }
}
