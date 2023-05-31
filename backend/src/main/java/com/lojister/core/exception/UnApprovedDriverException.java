package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnApprovedDriverException extends BaseException {

    public UnApprovedDriverException(String message) {
        super(message);
    }

    public UnApprovedDriverException(String message, String enMessage) {
        super(message, enMessage);
    }

}
