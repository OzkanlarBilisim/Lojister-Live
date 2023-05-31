package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdvertisementStatusException extends BaseException {

    public AdvertisementStatusException(String message) {
        super(message);
    }

    public AdvertisementStatusException(String message, String enMessage) {
        super(message, enMessage);
    }
}
