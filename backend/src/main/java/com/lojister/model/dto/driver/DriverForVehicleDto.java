package com.lojister.model.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverForVehicleDto {

    private String firstName;

    private String lastName;

    private String profilePhoto;

    private String profilePhotoFileName;

    private String profilePhotoType;

}
