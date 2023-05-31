package com.lojister.model.dto.clientadvertisement;

import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessMinimalDto;
import com.lojister.model.dto.dynamic.CargoTypeDto;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.enums.*;
import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ClientAdvertisementMinimalDto extends AbstractTimestampEntity {
    private LocalDate adStartingDate;
    private LocalDate adDueDate;
    private AdvertisementAddress startingAddress;
    private AdvertisementAddress dueAddress;
    private AdvertisementAddress containerAddress;
    private ContainerType containerType;
    private Recipient containerRecipient;
    private SimpleAdvertisementAddress simpleContainerAddress;
    private ClientAdvertisementType clientAdvertisementType;
    private AdvertisementStatus advertisementStatus;
    private AdvertisementProcessStatus advertisementProcessStatus;
    private String clientAdvertisementCode;
    private RegionAdvertisementType regionAdvertisementType;
    private Set<CargoTypeDto> cargoTypes = new LinkedHashSet<>();
    private ClientTransportProcessMinimalDto clientTransportProcess;
    private AdvertisementTransportType advertisementTransportType;

}
