package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignedDriverToVehicleException extends BaseException {

    public AssignedDriverToVehicleException(String message) {
        super(message);
    }

    public AssignedDriverToVehicleException(String message, String enMessage) {
        super(message, enMessage);
    }
}
