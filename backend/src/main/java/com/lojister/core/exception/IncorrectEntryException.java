package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncorrectEntryException extends BaseException {

    public IncorrectEntryException(String message) {
        super(message);
    }

    public IncorrectEntryException(String message, String enMessage) {
        super(message, enMessage);
    }

}
