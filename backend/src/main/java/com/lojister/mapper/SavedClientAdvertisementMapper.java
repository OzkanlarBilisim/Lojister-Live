package com.lojister.mapper;

import com.lojister.model.dto.SavedClientAdvertisementResponseDto;
import com.lojister.model.entity.SavedClientAdvertisement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {VehicleTypeMapper.class,TrailerTypeMapper.class,
                                            TrailerFloorTypeMapper.class,TrailerFeatureMapper.class,PackagingTypeMapper.class,
                                            CargoTypeMapper.class,LoadTypeMapper.class,CurrencyUnitMapper.class})
public interface SavedClientAdvertisementMapper extends BaseMapper<SavedClientAdvertisementResponseDto, SavedClientAdvertisement>{

}
