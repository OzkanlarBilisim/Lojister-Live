package com.lojister.repository.address;

import com.lojister.model.dto.addresses.DistrictDto;
import com.lojister.model.entity.adresses.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {

    @Query(value = "select new com.lojister.model.dto.addresses.DistrictDto(dsc.id,dsc.districtName) "+
            "from "+ "District dsc "+"where "+"dsc.provinceName=:provinceName "+" order by dsc.districtName asc ")
    List<DistrictDto> findByProvinceNameDto(@Param(value = "provinceName")String provinceName);

    @Query(value = "select new com.lojister.model.dto.addresses.DistrictDto(dsc.id,dsc.districtName) "+
            "from "+ "District dsc "+"where "+"dsc.provinceId=:provinceId")
    List<DistrictDto> findByProvinceIdDto(@Param(value = "provinceId")Long provinceId);

    List<District> findByProvinceId(Long provinceId);

    List<District> findByDistrictNameContains(String districtName);

    Optional<District> findByProvinceNameIgnoreCaseAndDistrictNameIgnoreCase(String provinceName, String districtName);

    Optional<District> findByDistrictName(String districtName);

}
