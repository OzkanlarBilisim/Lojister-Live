package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpiredVerificationTokenException extends BaseException {

    public ExpiredVerificationTokenException(String message) {
        super(message);
    }

    public ExpiredVerificationTokenException(String message, String enMessage) {
        super(message, enMessage);
    }
}
