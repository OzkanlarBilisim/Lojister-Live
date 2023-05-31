package com.lojister.mapper;

import com.lojister.model.dto.VerificationTokenDto;
import com.lojister.model.entity.VerificationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = UserMapper.class)
public interface VerificationTokenMapper extends BaseMapper<VerificationTokenDto, VerificationToken> {

}
