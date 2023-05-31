package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnApprovedAccountException extends BaseException {

    public UnApprovedAccountException(String message) {
        super(message);
    }

    public UnApprovedAccountException(String message, String enMessage) {
        super(message, enMessage);
    }

}
