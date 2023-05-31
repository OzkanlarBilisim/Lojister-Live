package com.lojister.core.exception.validator;

import com.lojister.core.exception.ValidatorException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicatePhoneException extends ValidatorException {

    public DuplicatePhoneException(String message) {
        super(message);
    }

}
