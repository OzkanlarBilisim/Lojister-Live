package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateRatingException extends BaseException {

    public DuplicateRatingException(String message) {
        super(message);
    }

    public DuplicateRatingException(String message, String enMessage) {
        super(message, enMessage);
    }

}
