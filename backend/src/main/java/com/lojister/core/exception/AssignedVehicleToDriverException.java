package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignedVehicleToDriverException extends BaseException {

    public AssignedVehicleToDriverException(String message) {
        super(message);
    }

    public AssignedVehicleToDriverException(String message, String enMessage) {
        super(message, enMessage);
    }
}
