package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongTransportProcessCode extends BaseException {

    public WrongTransportProcessCode(String message) {
        super(message);
    }

    public WrongTransportProcessCode(String message, String enMessage) {
        super(message, enMessage);
    }
}
