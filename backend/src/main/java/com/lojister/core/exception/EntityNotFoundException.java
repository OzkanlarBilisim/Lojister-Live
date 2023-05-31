package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, String enMessage) {
        super(message, enMessage);
    }
}
