package com.lojister.repository.address;

import com.lojister.model.dto.addresses.ProvinceDto;
import com.lojister.model.entity.adresses.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query(value = "select new com.lojister.model.dto.addresses.ProvinceDto(prv.id,prv.provinceName) " +
            "from " + "Province prv order by prv.provinceName asc ")
    List<ProvinceDto> findAllProvinceDto();

    List<Province> findByProvinceNameContains(String provinceName);

    Optional<Province> findByProvinceNameIgnoreCase(String provinceName);

}
