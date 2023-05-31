package com.lojister.model.dto;

import com.lojister.model.entity.User;
import lombok.Data;

@Data
public class UpdateUserEmailCheckDto {

    private User user;

    private String email;

    /**
     * @param user değiştirilecek user
     * @param email yeni email adresi
     */
    public UpdateUserEmailCheckDto(User user, String email) {
        this.user = user;
        this.email = email;
    }
}
