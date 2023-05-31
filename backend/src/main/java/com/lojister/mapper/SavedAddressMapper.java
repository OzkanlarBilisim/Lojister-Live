package com.lojister.mapper;

import com.lojister.model.dto.SavedAddressDto;
import com.lojister.model.entity.SavedAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface SavedAddressMapper extends BaseMapper<SavedAddressDto, SavedAddress>{
}
