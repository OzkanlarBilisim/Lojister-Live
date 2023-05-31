package com.lojister.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class SimpleAdvertisementAddress {

    private String country;
    private String province;
    private String district;
    @Column(length = 30)
    private String zipCode;
    private String neighborhood;
    private Double lat = 0D;
    private Double lng = 0D;
}
