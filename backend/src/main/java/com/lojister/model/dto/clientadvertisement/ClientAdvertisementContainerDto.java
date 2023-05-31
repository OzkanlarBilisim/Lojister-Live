package com.lojister.model.dto.clientadvertisement;

import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessForClientAdvertisementDto;
import com.lojister.model.dto.dynamic.*;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.ContainerType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class ClientAdvertisementContainerDto {
    private ClientDto client;
    private ClientTransportProcessForClientAdvertisementDto clientTransportProcess;
    private LocalDate adStartingDate;
    private LocalDate adDueDate;
    private LocalTime adStartingTime;
    private LocalTime adDueTime;
    private AdvertisementAddress startingAddress;
    private AdvertisementAddress dueAddress;
    private Recipient startRecipient;
    private Recipient dueRecipient;
    private String tonnage;
    private Boolean isPorter;
    private Boolean isStacking;
    private String documentNo;
    private String explanation;
    private AdvertisementStatus advertisementStatus;
    private LocalDateTime createdDateTime;
    private CurrencyUnitDto currencyUnit;
    private AdvertisementAddress containerAddress;
    private ContainerType containerType;
    private Recipient containerRecipient;
}
