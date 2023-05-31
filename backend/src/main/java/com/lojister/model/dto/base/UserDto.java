package com.lojister.model.dto.base;

import com.lojister.model.enums.Role;
import lombok.Data;

@Data
public class UserDto extends BaseDto{

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Role role;

}
