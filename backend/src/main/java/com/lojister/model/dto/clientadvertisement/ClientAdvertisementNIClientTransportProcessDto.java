package com.lojister.model.dto.clientadvertisement;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.dynamic.*;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.ClientAdvertisementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAdvertisementNIClientTransportProcessDto extends BaseDto {

    private ClientDto client;

    private ClientAdvertisementType clientAdvertisementType;

    private Long piece;

    private Long vehicleCount;

    private Set<VehicleTypeDto> vehicleTypes = new LinkedHashSet<>();

    private Set<TrailerTypeDto> trailerTypes= new LinkedHashSet<>();

    private Set<TrailerFloorTypeDto> trailerFloorTypes=  new LinkedHashSet<>();

    private Set<TrailerFeatureDto> trailerFeatures=  new LinkedHashSet<>();

    private LocalDate adStartingDate;

    private LocalDate adDueDate;

    private LocalTime adStartingTime;

    private LocalTime adDueTime;

    private AdvertisementAddress startingAddress;

    private AdvertisementAddress dueAddress;

    private Recipient startRecipient;

    private Recipient dueRecipient;

    private PackagingTypeDto packagingType;

    private Set<CargoTypeDto> cargoTypes = new LinkedHashSet<>();

    private String volume;

    private String desi;

    private String ldm;

    private Double goodsPrice;

    private String tonnage;

    private Set<LoadTypeDto> loadType = new LinkedHashSet<>();

    private Boolean isPorter;

    private Boolean isStacking;

    private String documentNo;

    private String explanation;

    private Double height; // yükseklik

    private Double length; // uzunluk

    private Double width;  // en

    private AdvertisementStatus advertisementStatus;

    private String clientAdvertisementCode;

    private CurrencyUnitDto currencyUnit;

}
