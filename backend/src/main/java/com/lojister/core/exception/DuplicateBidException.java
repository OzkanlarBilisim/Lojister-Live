package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateBidException extends BaseException {

    public DuplicateBidException(String message) {
        super(message);
    }

    public DuplicateBidException(String message, String enMessage) {
        super(message, enMessage);
    }

}
