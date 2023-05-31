package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongQrCodeException extends BaseException {

    public WrongQrCodeException(String message) {
        super(message);
    }

    public WrongQrCodeException(String message, String enMessage) {
        super(message, enMessage);
    }
}
