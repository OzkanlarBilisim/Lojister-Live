package com.lojister.mapper;

import com.lojister.model.dto.InsuredTransportProcessResponseDto;
import com.lojister.model.entity.InsuredTransportProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClientTransportProcessMapper.class})
public interface InsuredTransportProcessMapper extends BaseMapper<InsuredTransportProcessResponseDto, InsuredTransportProcess> {
}
