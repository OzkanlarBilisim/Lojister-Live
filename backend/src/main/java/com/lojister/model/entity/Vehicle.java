package com.lojister.model.entity;

import com.lojister.model.enums.VehicleStatus;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.adminpanel.TrailerFeature;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.model.entity.adminpanel.TrailerType;
import com.lojister.model.entity.adminpanel.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends AbstractTimestampEntity {

    private String vehicleName;

    private Long vehicleCount;

    private String brand;

    private String vehicleModel;

    private String licencePlate;

    @ColumnDefault("''")
    private String trailerPlate = "";

    private Double maxCapacity;

    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;

    @ColumnDefault("''")
    private String statusDescription = "";

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "vehicletype_id")
    private VehicleType vehicleType;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "trailertype_id")
    private TrailerType trailerType;

    @ManyToOne
    @JoinColumn(name = "trailerfloortype_id")
    private TrailerFloorType trailerFloorType;

    @ManyToMany
    private Set<TrailerFeature> trailerFeature = new LinkedHashSet<>();

    @Embedded
    private Position lastPosition;

}
