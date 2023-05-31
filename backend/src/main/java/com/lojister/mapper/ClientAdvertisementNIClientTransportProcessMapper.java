package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementNIClientTransportProcessDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses ={ClientMapper.class,VehicleTypeMapper.class,TrailerTypeMapper.class,TrailerFloorTypeMapper.class,
                                TrailerFeatureMapper.class,PackagingTypeMapper.class,CargoTypeMapper.class, LoadTypeMapper.class, CurrencyUnitMapper.class }   )
public interface ClientAdvertisementNIClientTransportProcessMapper extends BaseMapper<ClientAdvertisementNIClientTransportProcessDto,ClientAdvertisement>{
}
