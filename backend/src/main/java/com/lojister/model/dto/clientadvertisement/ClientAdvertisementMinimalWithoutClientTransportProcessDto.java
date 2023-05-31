package com.lojister.model.dto.clientadvertisement;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.dynamic.CargoTypeDto;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.ClientAdvertisementType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class ClientAdvertisementMinimalWithoutClientTransportProcessDto extends BaseDto {
    private LocalDate adStartingDate;
    private LocalDate adDueDate;
    private AdvertisementAddress startingAddress;
    private AdvertisementAddress dueAddress;
    private ClientAdvertisementType clientAdvertisementType;
    private AdvertisementProcessStatus advertisementProcessStatus;
    private String clientAdvertisementCode;
    private Set<CargoTypeDto> cargoTypes = new LinkedHashSet<>();
}
