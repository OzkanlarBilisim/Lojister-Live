package com.lojister.core.exception.validator;

import com.lojister.core.exception.ValidatorException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateMailException extends ValidatorException {

    public DuplicateMailException(String message) {
        super(message);
    }

}
