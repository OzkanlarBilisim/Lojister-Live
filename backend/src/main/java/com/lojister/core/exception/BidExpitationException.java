package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidExpitationException extends BaseException {
    public BidExpitationException(String message) {
        super(message);
    }

}
