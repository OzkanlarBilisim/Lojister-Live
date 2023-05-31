package com.lojister.core.validator.base;

import com.lojister.core.exception.ValidatorException;

public interface Validator<T> {

    void validate(T t) throws ValidatorException;
}
