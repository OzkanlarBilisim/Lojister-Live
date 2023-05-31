package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelException extends BaseException {

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, String enMessage) {
        super(message, enMessage);
    }

}
