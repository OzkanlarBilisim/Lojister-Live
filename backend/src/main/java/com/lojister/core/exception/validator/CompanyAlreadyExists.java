package com.lojister.core.exception.validator;

import com.lojister.core.exception.ValidatorException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyAlreadyExists extends ValidatorException {

    public CompanyAlreadyExists(String message) {
        super(message);
    }

}


