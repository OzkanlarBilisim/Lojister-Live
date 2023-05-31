package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientAdvertisementBidUniqueConstraintException extends BaseException {

    public ClientAdvertisementBidUniqueConstraintException(String message) {
        super(message);
    }

    public ClientAdvertisementBidUniqueConstraintException(String message, String enMessage) {
        super(message, enMessage);
    }

}
