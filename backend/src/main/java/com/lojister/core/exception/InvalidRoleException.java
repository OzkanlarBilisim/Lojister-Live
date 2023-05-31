package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidRoleException extends BaseException {

    public InvalidRoleException(String message) {
        super(message);
    }

    public InvalidRoleException(String message, String enMessage) {
        super(message, enMessage);
    }

}
