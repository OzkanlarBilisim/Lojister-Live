package com.lojister.model.dto;

import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.client.Client;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Builder
public class CreateClientAdvertisementDto {

    private Client client;
    private Set<TrailerType> trailerTypeSet;
    private Set<TrailerFloorType> trailerFloorTypeSet;
    private Set<TrailerFeature> trailerFeatureSet;
    private AdvertisementAddress startingAddress;
    private AdvertisementAddress dueAddress;
    private Recipient startRecipient;
    private Recipient dueRecipient;
    private LocalDate adStartingDate;
    private LocalDate adDueDate;
    private LocalTime adStartingTime;
    private LocalTime adDueTime;
    private PackagingType packagingType;
    private Set<CargoType> cargoTypeSet;
    private Set<LoadType> loadTypes;
    private String tonnage;
    private Double goodsPrice;
    private Boolean isPorter;
    private Boolean isStacking;
    private String documentNo;
    private String explanation;
    private CurrencyUnit currencyUnit;
    private String volume;
    private String desi;
    private String ldm;
}
