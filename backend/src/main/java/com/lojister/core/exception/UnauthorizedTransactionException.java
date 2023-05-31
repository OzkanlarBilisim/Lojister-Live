package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnauthorizedTransactionException extends BaseException {

    public UnauthorizedTransactionException(String message) {
        super(message);
    }

    public UnauthorizedTransactionException(String message, String enMessage) {
        super(message, enMessage);
    }

}
