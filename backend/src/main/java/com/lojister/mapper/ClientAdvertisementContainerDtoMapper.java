package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementContainerDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementDto;
import com.lojister.model.entity.client.ClientAdvertisementContainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientAdvertisementContainerDtoMapper extends BaseMapper<ClientAdvertisementContainerDto, ClientAdvertisementContainer>{

    ClientAdvertisementDto customEntity(ClientAdvertisementContainer clientAdvertisementContainer);
}
