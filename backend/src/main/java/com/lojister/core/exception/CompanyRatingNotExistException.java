package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyRatingNotExistException extends BaseException {

    public CompanyRatingNotExistException(String message) {
        super(message);
    }

    public CompanyRatingNotExistException(String message, String enMessage) {
        super(message, enMessage);
    }
}
