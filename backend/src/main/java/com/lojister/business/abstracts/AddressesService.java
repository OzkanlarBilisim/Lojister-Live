package com.lojister.business.abstracts;

import com.lojister.model.dto.addresses.AddressesDto;
import com.lojister.model.dto.addresses.DistrictDto;
import com.lojister.model.dto.addresses.NeighborhoodDto;
import com.lojister.model.dto.addresses.ProvinceDto;
import com.lojister.model.entity.adresses.District;
import com.lojister.model.entity.adresses.Neighborhood;
import com.lojister.model.entity.adresses.Province;

import java.util.List;

public interface AddressesService {

    Province getProvinceById(Long id);

    List<ProvinceDto> getAllProvince();

    District getDistrictById(Long id);

    List<DistrictDto> getAllDistrictByProvinceId(Long provinceId);

    List<DistrictDto> getAllDistrictByProvinceName(String provinceName);

    Neighborhood getByNeighborhoodId(Long id);

    List<NeighborhoodDto> getAllNeighborhoodByDistrictId(Long districtId);

    List<NeighborhoodDto> getAllNeighborhoodByProvinceNameAndDistrictName(String provinceName,String districtName);

    AddressesDto findByWord(String word);

}
