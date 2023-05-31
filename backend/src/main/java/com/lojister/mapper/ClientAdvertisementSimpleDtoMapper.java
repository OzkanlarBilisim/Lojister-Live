package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementSimpleDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementContainer;
import com.lojister.model.entity.client.ClientAdvertisementFtl;
import com.lojister.model.entity.client.ClientAdvertisementPartial;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientAdvertisementSimpleDtoMapper extends BaseMapper<ClientAdvertisementSimpleDto, ClientAdvertisement>{

    @Mapping(source="simpleStartingAddress",target="startingAddress")
    @Mapping(source="simpleDueAddress",target="dueAddress")
    @Named("entityToPartialHideDto")
    ClientAdvertisementSimpleDto entityToPartialHideDto(ClientAdvertisementPartial clientAdvertisementPartial);

    @IterableMapping(qualifiedByName = "entityToPartialHideDto")
    List<ClientAdvertisementSimpleDto> entityListToPartialHideDtoList(List<ClientAdvertisementPartial> entityList);


    @Mapping(source="simpleStartingAddress",target="startingAddress")
    @Mapping(source="simpleDueAddress",target="dueAddress")
    @Named("entityToFtlHideDto")
    ClientAdvertisementSimpleDto entityToFtlHideDto(ClientAdvertisementFtl clientAdvertisementFtl);

    @IterableMapping(qualifiedByName = "entityToFtlHideDto")
    List<ClientAdvertisementSimpleDto> entityListToFtlHideDtoList(List<ClientAdvertisementFtl> entityList);

    @Mapping(source="simpleStartingAddress",target="startingAddress")
    @Mapping(source="simpleDueAddress",target="dueAddress")
    @Named("entityToContainerHideDto")
    ClientAdvertisementSimpleDto entityToContainerHideDto(ClientAdvertisementContainer clientAdvertisementContainer);

    @IterableMapping(qualifiedByName = "entityToFtlHideDto")
    List<ClientAdvertisementSimpleDto> entityListToContainerHideDtoList(List<ClientAdvertisementContainer> entityList);



    @Named("entityToPartialDto")
    ClientAdvertisementSimpleDto entityToPartialDto(ClientAdvertisementPartial clientAdvertisementPartial);

    @IterableMapping(qualifiedByName = "entityToPartialHideDto")
    List<ClientAdvertisementSimpleDto> entityListToPartialDtoList(List<ClientAdvertisementPartial> entityList);

    @Named("entityToFtlDto")
    ClientAdvertisementSimpleDto entityToFtlDto(ClientAdvertisementFtl clientAdvertisementFtl);

    @IterableMapping(qualifiedByName = "entityToFtlDto")
    List<ClientAdvertisementSimpleDto> entityListToFtlDtoList(List<ClientAdvertisementFtl> entityList);
}
