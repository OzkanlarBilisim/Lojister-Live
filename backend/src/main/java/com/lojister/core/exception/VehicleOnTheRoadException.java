package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleOnTheRoadException extends BaseException {

    public VehicleOnTheRoadException(String message) {
        super(message);
    }

    public VehicleOnTheRoadException(String message, String enMessage) {
        super(message, enMessage);
    }
}
