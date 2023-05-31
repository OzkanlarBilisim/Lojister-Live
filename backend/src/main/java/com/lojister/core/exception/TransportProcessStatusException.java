package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransportProcessStatusException extends BaseException {

    public TransportProcessStatusException(String message) {
        super(message);
    }

    public TransportProcessStatusException(String message, String enMessage) {
        super(message, enMessage);
    }
}
