package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongTransportProcessType extends BaseException {

    public WrongTransportProcessType(String message) {
        super(message);
    }

    public WrongTransportProcessType(String message, String enMessage) {
        super(message, enMessage);
    }
}
