package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ClientAdvertisementMinimalDtoMapper extends BaseMapper<ClientAdvertisementMinimalDto, ClientAdvertisement>{

    @Mapping(source="simpleStartingAddress",target="startingAddress")
    @Mapping(source="simpleDueAddress",target="dueAddress")
    @Named("customDtoToEntity")
    ClientAdvertisementMinimalDto customEntityToDto(ClientAdvertisement clientAdvertisement);

    @IterableMapping(qualifiedByName = "customDtoToEntity")
    List<ClientAdvertisementMinimalDto> customEntityListToDtoList(List<ClientAdvertisement> entityList);

}
