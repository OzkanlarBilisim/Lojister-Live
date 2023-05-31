package com.lojister.model.dto;

import com.lojister.model.entity.User;
import lombok.Data;



@Data
public class UpdateUserPhoneCheckDto {

    private User user;

    private String phone;

    /**
     *
     * @param user değiştirilecek user
     * @param phone yeni telefon numarası
     */
    public UpdateUserPhoneCheckDto(User user, String phone) {
        this.user = user;
        this.phone = phone;
    }
}
