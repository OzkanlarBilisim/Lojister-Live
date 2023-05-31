package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileSizeMaxException extends BaseException {

    public FileSizeMaxException(String message) {
        super(message);
    }

    public FileSizeMaxException(String message, String enMessage) {
        super(message, enMessage);
    }
}
