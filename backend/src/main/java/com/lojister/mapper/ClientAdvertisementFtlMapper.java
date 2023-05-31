package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementDto;
import com.lojister.model.entity.client.ClientAdvertisementFtl;
import com.lojister.model.entity.adminpanel.VehicleType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ClientMapper.class, ClientTransportProcessForClientAdvertisementMapper.class, VehicleType.class, TrailerTypeMapper.class,
        TrailerFloorTypeMapper.class, TrailerFeatureMapper.class, PackagingTypeMapper.class, CargoTypeMapper.class,
        LoadTypeMapper.class,CurrencyUnitMapper.class})

public interface ClientAdvertisementFtlMapper extends BaseMapper<ClientAdvertisementDto, ClientAdvertisementFtl>{
}
