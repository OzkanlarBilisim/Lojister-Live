package com.lojister.repository.address;

import com.lojister.model.dto.addresses.NeighborhoodDto;
import com.lojister.model.entity.adresses.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {


    @Query(value = "select new com.lojister.model.dto.addresses.NeighborhoodDto(ngh.id,ngh.neighborhoodName) " +
            "from " + "Neighborhood ngh " + "where " + "ngh.provinceName=:provinceName and ngh.districtName=:districtName "+ "order by ngh.neighborhoodName asc ")
    List<NeighborhoodDto> findByProvinceNameAndDistrictNameDto(@Param(value = "provinceName") String provinceName,@Param(value = "districtName") String districtName);

    @Query(value = "select new com.lojister.model.dto.addresses.NeighborhoodDto(ngh.id,ngh.neighborhoodName) " +
            "from " + "Neighborhood ngh " + "where " + "ngh.districtId=:districtId")
    List<NeighborhoodDto> findByDistrictIdDto(@Param(value = "districtId") Long districtId);

    List<Neighborhood> findByDistrictId(Long districtId);

    List<Neighborhood> findByNeighborhoodNameContains(String neighborhoodName);

    Optional<Neighborhood> findByNeighborhoodName(String neighborhoodName);

    Optional<Neighborhood> findFirstByProvinceNameIgnoreCaseAndDistrictNameIgnoreCaseAndNeighborhoodNameContainsIgnoreCase(String provinceName, String districtName,String neighborhoodName);

}
