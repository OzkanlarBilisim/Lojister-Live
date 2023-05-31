package com.lojister.model.entity.adminpanel;

import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoType extends BaseEntity {

    @Column(unique = true)
    private String typeName;

    @Column(unique = true)
    private String engTypeName;

    @Enumerated(EnumType.STRING)
    private DynamicStatus dynamicStatus;

    public CargoType(String typeName, DynamicStatus dynamicStatus) {
        this.typeName = typeName;
        this.dynamicStatus = dynamicStatus;
    }

    public static CargoType create(String trTypeName,String engTypeName){
        CargoType cargoType = new CargoType();
        cargoType.typeName = trTypeName;
        cargoType.engTypeName = engTypeName;
        cargoType.dynamicStatus = DynamicStatus.ACTIVE;
        return cargoType;
    }

}
