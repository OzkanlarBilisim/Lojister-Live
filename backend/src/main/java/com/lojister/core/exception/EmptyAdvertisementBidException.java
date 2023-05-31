package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmptyAdvertisementBidException extends BaseException {

    public EmptyAdvertisementBidException(String message) {
        super(message);
    }

    public EmptyAdvertisementBidException(String message, String enMessage) {
        super(message, enMessage);
    }

}
