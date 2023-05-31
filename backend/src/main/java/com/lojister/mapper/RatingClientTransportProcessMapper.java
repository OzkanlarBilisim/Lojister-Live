package com.lojister.mapper;

import com.lojister.model.dto.RatingClientTransportProcessDto;
import com.lojister.model.entity.RatingClientTransportProcess;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.sql.Wrapper;
import java.util.List;


@Mapper(componentModel = "spring",uses = {ClientTransportProcessMapper.class})
public interface RatingClientTransportProcessMapper extends BaseMapper<RatingClientTransportProcessDto, RatingClientTransportProcess>{

}
