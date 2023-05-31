package com.lojister.core.exception.validator;

import com.lojister.core.exception.ValidatorException;
public class DuplicateCitizenIdException extends ValidatorException {

    public DuplicateCitizenIdException(String message) {
        super(message);
    }
}
