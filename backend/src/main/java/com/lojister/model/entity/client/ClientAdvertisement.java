package com.lojister.model.entity.client;

import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.enums.*;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ClientAdvertisementType", discriminatorType = DiscriminatorType.STRING)
public class ClientAdvertisement extends AbstractTimestampEntity {
    public ClientAdvertisement() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "ClientAdvertisementType", nullable = false, insertable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private ClientAdvertisementType clientAdvertisementType;
    /////
    @OneToOne(fetch = FetchType.EAGER)
    private ClientTransportProcess clientTransportProcess;

    private Double length; // uzunluk

    @Column(unique = true)
    private String clientAdvertisementCode;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<VehicleType> vehicleTypes = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<TrailerType> trailerTypes = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<TrailerFloorType> trailerFloorTypes = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<TrailerFeature> trailerFeatures = new LinkedHashSet<>();

    private LocalDate adStartingDate;

    private LocalDate adDueDate;

    private LocalTime adStartingTime;

    private LocalTime adDueTime;

    //-------------------------------------------------------------------------------

    @Embedded
    private AdvertisementAddress startingAddress;

    @Embedded
    private SimpleAdvertisementAddress simpleStartingAddress;

    @Embedded
    private AdvertisementAddress dueAddress;

    @Embedded
    private SimpleAdvertisementAddress simpleDueAddress;

    @Embedded
    private Recipient startRecipient;

    @Embedded
    private Recipient dueRecipient;

    //---------------------------------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packagingType_id")
    private PackagingType packagingType;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<CargoType> cargoTypes = new LinkedHashSet<>();

    private String volume;

    private String desi;

    private String ldm;

    private Double goodsPrice;

    private String tonnage;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<LoadType> loadType = new LinkedHashSet<>();

    private Boolean isPorter = false;

    private Boolean isStacking = false;

    private String documentNo;

    private String explanation;

    @Enumerated(value = EnumType.STRING)
    private AdvertisementStatus advertisementStatus = AdvertisementStatus.ACTIVE;

    @Enumerated(value = EnumType.STRING)
    private AdvertisementProcessStatus advertisementProcessStatus = AdvertisementProcessStatus.WAITING;

    @Enumerated(value = EnumType.STRING)
    private RegionAdvertisementType regionAdvertisementType;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 40)
    private AdvertisementTransportType advertisementTransportType;

    @ManyToOne
    private CurrencyUnit currencyUnit;

    @PostLoad
    private void timeOutCheck() {
        if(getAdvertisementProcessStatus()==AdvertisementProcessStatus.WAITING||getAdvertisementProcessStatus()==AdvertisementProcessStatus.BID_GIVEN){
            if (Optional.ofNullable(adStartingDate).isPresent()&&Optional.ofNullable(adStartingTime).isPresent()) {
                if(LocalDateTime.now().isAfter(LocalDateTime.parse(adStartingDate.format(DateTimeFormatter.ISO_LOCAL_DATE)+"T"+adStartingTime.format(DateTimeFormatter.ISO_LOCAL_TIME)))){
                    setAdvertisementProcessStatus(AdvertisementProcessStatus.TIMEOUT);
                    setAdvertisementStatus(AdvertisementStatus.TIMEOUT);
                }
            }
        }


    }


    public void nullCheckAndSetEmptyString(){
        this.startingAddress.nullCheck();
        this.dueAddress.nullCheck();
    }

}
