package com.lojister.core.exception;

import com.lojister.core.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateAdminPanelEntityException extends BaseException {

    public DuplicateAdminPanelEntityException(String message) {
        super(message);
    }

    public DuplicateAdminPanelEntityException(String message, String enMessage) {
        super(message, enMessage);
    }
}
