package com.lojister.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseMapper<Dto,Entity> {

    @Named("dtoToEntity")
    Entity dtoToEntity(Dto dto);

    @Named("entityToDto")
    Dto entityToDto(Entity entity);

    @IterableMapping(qualifiedByName = "entityToDto")
    List<Dto> entityListToDtoList(List<Entity> entityList);

    @IterableMapping(qualifiedByName = "dtoToEntity")
    List<Entity> dtoListToEntityList(List<Dto> dtoList);

}
