package com.lojister.model.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Address {

    @ColumnDefault("''")
    private String country;

    @ColumnDefault("''")
    private String province;

    @ColumnDefault("''")
    private String district;

    @ColumnDefault("''")
    private String zipCode;

    @ColumnDefault("''")
    private String neighborhood;

    @ColumnDefault("''")
    private String fullAddress;

}
