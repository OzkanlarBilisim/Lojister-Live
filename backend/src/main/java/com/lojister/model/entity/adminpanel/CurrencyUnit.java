package com.lojister.model.entity.adminpanel;

import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
@NoArgsConstructor
public class CurrencyUnit extends BaseEntity {

    @Column(unique = true)
    private String currencyName;

    @Column(unique = true)
    private String currencyAbbreviation;

    private String currencySymbol;

    @Enumerated(EnumType.STRING)
    private DynamicStatus dynamicStatus;

    public CurrencyUnit(String currencyName,
                        String currencyAbbreviation,
                        String currencySymbol,
                        DynamicStatus dynamicStatus) {
        this.currencyName = currencyName;
        this.currencyAbbreviation = currencyAbbreviation;
        this.currencySymbol = currencySymbol;
        this.dynamicStatus = dynamicStatus;
    }
}
