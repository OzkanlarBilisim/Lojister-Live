package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NullPointerExceptionHandler extends BaseException {

    public NullPointerExceptionHandler(String message) {
        super(message);
    }

    public NullPointerExceptionHandler(String message, String enMessage) {
        super(message, enMessage);
    }

}
