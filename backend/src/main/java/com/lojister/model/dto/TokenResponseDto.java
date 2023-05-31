package com.lojister.model.dto;

import com.lojister.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    private String username;
    private String token;
    private Role role;

}
