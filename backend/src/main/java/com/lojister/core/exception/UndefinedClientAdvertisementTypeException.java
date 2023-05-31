package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UndefinedClientAdvertisementTypeException extends BaseException {

    public UndefinedClientAdvertisementTypeException(String message) {
        super(message);
    }

    public UndefinedClientAdvertisementTypeException(String message, String enMessage) {
        super(message, enMessage);
    }

}
