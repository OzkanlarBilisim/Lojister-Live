package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountNotActiveException extends BaseException {

    public AccountNotActiveException(String message) {
        super(message);
    }

    public AccountNotActiveException(String message, String enMessage) {
        super(message, enMessage);
    }
}
