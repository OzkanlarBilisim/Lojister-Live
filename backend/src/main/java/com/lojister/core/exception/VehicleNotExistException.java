package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleNotExistException extends BaseException {

    public VehicleNotExistException(String message) {
        super(message);
    }

    public VehicleNotExistException(String message, String enMessage) {
        super(message, enMessage);
    }
}
