package com.lojister.core.exception;


import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SizeException extends BaseException {

    public SizeException(String message) {
        super(message);
    }

    public SizeException(String message, String enMessage) {
        super(message, enMessage);
    }

}

