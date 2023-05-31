package com.lojister.model.dto.client;

import com.lojister.model.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateResponseDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String token;

}
