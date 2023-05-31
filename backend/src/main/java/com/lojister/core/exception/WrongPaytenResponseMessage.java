package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongPaytenResponseMessage extends BaseException {

    public WrongPaytenResponseMessage(String message) {
        super(message);
    }

    public WrongPaytenResponseMessage(String message, String enMessage) {
        super(message, enMessage);
    }
}
