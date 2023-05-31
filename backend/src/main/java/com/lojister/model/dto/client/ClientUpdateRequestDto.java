package com.lojister.model.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateRequestDto {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

}
