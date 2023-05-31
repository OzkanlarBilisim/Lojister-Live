package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.base.UserDto;
import com.lojister.model.enums.RegionType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SavedAddressDto extends BaseDto {

    @NotNull(message = "Adres başlığı boş olamaz")
    @NotEmpty(message = "Adres başlığı boş olamaz")
    private String addressName;

    @NotNull(message = "Şehir boş olamaz")
    @Size(min = 3, max = 255,message = "Şehir en az 3 karakter olmalıdır.")
    private String province;

    @Size(min = 2,max = 255,message = "İlçe en az 2 karakter olmalıdır.")
    private String district;

    @Size(min = 2,max = 255,message = "Mahalle en az 2 karakter olmalıdır.")
    private String neighborhood;

    @NotNull(message = "Ülke boş olamaz")
    @Size(min = 3, max = 255,message = "Ülke en az 3 karakter olmalıdır.")
    private String country;

    @Size(min = 4, max = 25,message = "Ülke en az 4 karakter olmalıdır.")
    private String zipCode;

    @NotNull(message = "Adres tarifi boş olamaz")
    @NotEmpty(message = "Adres tarifi boş olamaz")
    private String fullAddress;

    private Double lat = 0D;

    private Double lng = 0D;

    private String commercialTitle;

    private String firstName;

    private String lastName;

    private Boolean isDefaultAddress;

    private RegionType regionType;

    @NotNull(message = "Telefon numarası boş olamaz")
    private String phone;

  //  private UserDto user;
}
