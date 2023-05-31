package com.lojister.model.dto.driver;

import com.lojister.model.dto.base.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverMinDto extends BaseDto {
    private String firstName;
    private String lastName;
}
