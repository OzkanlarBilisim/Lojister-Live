package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverAssignedAnotherVehicleException extends BaseException {

    public DriverAssignedAnotherVehicleException(String message) {
        super(message);
    }

    public DriverAssignedAnotherVehicleException(String message, String enMessage) {
        super(message, enMessage);
    }

}
