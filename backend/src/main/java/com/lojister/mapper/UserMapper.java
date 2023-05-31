package com.lojister.mapper;

import com.lojister.model.dto.base.UserDto;
import com.lojister.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDto, User>{


}
