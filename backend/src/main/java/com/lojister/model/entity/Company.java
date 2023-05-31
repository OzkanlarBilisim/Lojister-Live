package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class Company extends AbstractTimestampEntity {

    private String commercialTitle;

    private String taxNumber;

    private String taxAdministration;

    @ColumnDefault("''")
    private String phone;

    @ColumnDefault("''")
    private String mail;

    @ColumnDefault("''")
    private String financialStaffFirstname;

    @ColumnDefault("''")
    private String financialStaffLastname;

    @ColumnDefault("''")
    private String financialStaffPhone;

    private Double rating;

    @Column(columnDefinition = "bigint default 0")
    private Long numberOfRating = 0L;

    @Embedded
    private Address address;

    @OneToOne
    @JoinColumn(name = "bankInformation_id")
    private BankInformation bankInformation;

    @Column(columnDefinition = "double default 10.0")
    private Double commissionRate = 10.0;

}
