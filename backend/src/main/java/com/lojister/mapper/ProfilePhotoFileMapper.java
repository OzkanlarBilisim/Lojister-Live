package com.lojister.mapper;

import com.lojister.model.dto.ProfilePhotoFileDto;
import com.lojister.model.entity.ProfilePhotoFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface ProfilePhotoFileMapper extends BaseMapper<ProfilePhotoFileDto, ProfilePhotoFile>{
}
