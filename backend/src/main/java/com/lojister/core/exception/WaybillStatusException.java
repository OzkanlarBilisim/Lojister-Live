package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaybillStatusException extends BaseException {

    public WaybillStatusException(String message) {
        super(message);
    }

    public WaybillStatusException(String message, String enMessage) {
        super(message, enMessage);
    }
}
