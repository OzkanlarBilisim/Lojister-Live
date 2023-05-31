package com.lojister.model.entity;

import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.client.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedClientAdvertisement extends AbstractTimestampEntity {

    private String advertisementName;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(value = EnumType.STRING)
    private ClientAdvertisementType clientAdvertisementType;

    private Long piece;

    @ManyToMany
    private Set<VehicleType> vehicleTypeSet = new LinkedHashSet<>();

    @ManyToMany
    private Set<TrailerType> trailerTypeSet = new LinkedHashSet<>();

    @ManyToMany
    private Set<TrailerFloorType> trailerFloorTypeSet = new LinkedHashSet<>();

    @ManyToMany
    private Set<TrailerFeature> trailerFeatureSet = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "packagingType_id")
    private PackagingType packagingType;

    @ManyToMany
    private Set<CargoType> cargoTypeSet = new LinkedHashSet<>();

    private String volume;

    private String desi;

    private String ldm;

    private Double goodsPrice;

    private String tonnage;

    @ManyToMany
    private Set<LoadType> loadTypeSet = new LinkedHashSet<>();

    private Boolean isPorter = false;

    private Boolean isStacking = false;

    private String documentNo;

    private String explanation;

    //PARSİYEL SİPARİŞ VERME ÖZEL ALANLAR
    private Double height; // yükseklik

    private Double length; // uzunluk

    private Double width;  // en

    @ManyToOne
    private CurrencyUnit currencyUnit;

    @Embedded
    private AdvertisementAddress startingAddress;

    @Embedded
    private AdvertisementAddress dueAddress;

    @Embedded
    private Recipient startRecipient;

    @Embedded
    private Recipient dueRecipient;

    private Long vehicleCount;

}
