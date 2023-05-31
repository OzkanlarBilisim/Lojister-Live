package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadException extends BaseException {
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, String enMessage) {
        super(message, enMessage);
    }
}
