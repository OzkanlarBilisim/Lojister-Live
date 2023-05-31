package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientAdvertisementDeleteException extends BaseException {

    public ClientAdvertisementDeleteException(String message) {
        super(message);
    }

    public ClientAdvertisementDeleteException(String message, String enMessage) {
        super(message, enMessage);
    }

}
