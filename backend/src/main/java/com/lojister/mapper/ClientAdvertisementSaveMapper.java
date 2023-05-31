package com.lojister.mapper;

import com.lojister.controller.advertisement.SaveClientAdvertisementRequest;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.adminpanel.VehicleType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ClientMapper.class, VehicleType.class, TrailerTypeMapper.class,
        TrailerFloorTypeMapper.class, TrailerFeatureMapper.class, PackagingTypeMapper.class, CargoTypeMapper.class,
        LoadTypeMapper.class,CurrencyUnitMapper.class})
public interface ClientAdvertisementSaveMapper extends BaseMapper<SaveClientAdvertisementRequest, ClientAdvertisement> {

}
