package com.lojister.core.exception;


import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsException extends BaseException {

    public SmsException(String message) {
        super(message);
    }

    public SmsException(String message, String enMessage) {
        super(message, enMessage);
    }

}

