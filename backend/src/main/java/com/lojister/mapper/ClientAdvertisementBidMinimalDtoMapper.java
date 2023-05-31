package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidMinimalDto;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ClientAdvertisementBidMinimalDtoMapper extends BaseMapper<ClientAdvertisementBidMinimalDto, ClientAdvertisementBid>{

    @Named("customEntityToDto")
    @Mapping(source = "clientAdvertisement.advertisementProcessStatus",target = "advertisementProcessStatus")
    ClientAdvertisementBidMinimalDto customEntityToDto(ClientAdvertisementBid entity);

    @IterableMapping(qualifiedByName = "customEntityToDto")
    List<ClientAdvertisementBidMinimalDto> customEntityListToDtoList(List<ClientAdvertisementBid> entityList);

}
