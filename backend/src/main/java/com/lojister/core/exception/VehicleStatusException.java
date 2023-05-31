package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleStatusException extends BaseException {

    public VehicleStatusException(String message) {
        super(message);
    }

    public VehicleStatusException(String message, String enMessage) {
        super(message, enMessage);
    }
}
