package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WrongSortingType extends BaseException {

    public WrongSortingType(String message) {
        super(message);
    }

    public WrongSortingType(String message, String enMessage) {
        super(message, enMessage);
    }
}
