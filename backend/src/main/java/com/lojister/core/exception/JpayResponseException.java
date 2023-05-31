package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JpayResponseException extends BaseException {

    public JpayResponseException(String message) {
        super(message);
    }

    public JpayResponseException(String message, String enMessage) {
        super(message, enMessage);
    }
}
