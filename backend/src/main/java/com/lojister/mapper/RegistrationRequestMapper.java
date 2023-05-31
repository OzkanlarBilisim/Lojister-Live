package com.lojister.mapper;

import com.lojister.controller.account.RegistrationRequestDto;
import com.lojister.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationRequestMapper extends BaseMapper<RegistrationRequestDto, User> {


}
