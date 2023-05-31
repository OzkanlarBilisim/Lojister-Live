package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.enums.RegionType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class SavedAddress extends AbstractTimestampEntity {

    private String addressName;

    private String province;

    private String district;

    private String neighborhood;

    private String fullAddress;

    private String country;

    private String zipCode;

    private Double lat = 0D;

    private Double lng = 0D;

    private String commercialTitle;

    private String firstName;

    private String lastName;

    private String phone;

    private Boolean isDefaultAddress = Boolean.FALSE;

    private RegionType regionType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
