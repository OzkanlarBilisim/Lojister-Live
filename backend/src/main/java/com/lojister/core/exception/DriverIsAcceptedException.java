package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverIsAcceptedException extends BaseException {
    public DriverIsAcceptedException(String message) {
        super(message);
    }

    public DriverIsAcceptedException(String message, String enMessage) {
        super(message, enMessage);
    }
}
