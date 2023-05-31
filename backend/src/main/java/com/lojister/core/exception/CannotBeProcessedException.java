package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;

public class CannotBeProcessedException extends BaseException {
    public CannotBeProcessedException(String message) {
        super(message);
    }

    public CannotBeProcessedException(String message, String enMessage) {
        super(message, enMessage);
    }
}
