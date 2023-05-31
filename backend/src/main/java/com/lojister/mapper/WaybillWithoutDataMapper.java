package com.lojister.mapper;


import com.lojister.model.dto.WaybillWithoutDataDto;
import com.lojister.model.dto.WaybillWithoutDataWithEntityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClientTransportProcessMapper.class})
public interface WaybillWithoutDataMapper extends BaseMapper<WaybillWithoutDataDto, WaybillWithoutDataWithEntityDto> {
}
