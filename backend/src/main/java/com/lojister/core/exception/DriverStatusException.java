package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverStatusException extends BaseException {

    public DriverStatusException(String message) {
        super(message);
    }

    public DriverStatusException(String message, String enMessage) {
        super(message, enMessage);
    }
}
