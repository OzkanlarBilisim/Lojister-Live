package com.lojister.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Optional;


@Embeddable
@Data
public class AdvertisementAddress implements Serializable {

    private String country;
    //null
    private String province;

    private String district;

    private String neighborhood;

    private String street;

    private String buildingInformation;
    // null
    @Column(length = 30)
    private String zipCode;

    private String fullAddress;

    private Double lat = 0D;

    private Double lng = 0D;


    public AdvertisementAddress nullCheck() {
        this.country = Optional.ofNullable(this.country).isPresent() ? this.country : "";
        this.province = Optional.ofNullable(this.province).isPresent() ? this.province : "";
        this.district = Optional.ofNullable(this.district).isPresent() ? this.district : "";
        this.neighborhood = Optional.ofNullable(this.neighborhood).isPresent() ? this.neighborhood : "";
        this.street = Optional.ofNullable(this.street).isPresent() ? this.street : "";
        this.buildingInformation = Optional.ofNullable(this.buildingInformation).isPresent() ? this.buildingInformation : "";
        this.zipCode = Optional.ofNullable(this.zipCode).isPresent() ? this.zipCode : "";
        this.fullAddress = Optional.ofNullable(this.fullAddress).isPresent() ? this.fullAddress : "";
        return this;
    }

}
