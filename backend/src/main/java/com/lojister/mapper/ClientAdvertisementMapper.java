package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.adminpanel.VehicleType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ClientMapper.class, ClientTransportProcessForClientAdvertisementMapper.class,VehicleType.class, TrailerTypeMapper.class,
        TrailerFloorTypeMapper.class, TrailerFeatureMapper.class, PackagingTypeMapper.class, CargoTypeMapper.class,
        LoadTypeMapper.class,CurrencyUnitMapper.class})
public interface ClientAdvertisementMapper extends BaseMapper<ClientAdvertisementDto,ClientAdvertisement> {

}
