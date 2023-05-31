package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressNotFoundException extends BaseException {

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(String message, String enMessage) {
        super(message, enMessage);
    }
}
