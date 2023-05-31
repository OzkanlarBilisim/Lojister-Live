package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LastPositionNotExistException extends BaseException {

    public LastPositionNotExistException(String message) {
        super(message);
    }

    public LastPositionNotExistException(String message, String enMessage) {
        super(message, enMessage);
    }
}
