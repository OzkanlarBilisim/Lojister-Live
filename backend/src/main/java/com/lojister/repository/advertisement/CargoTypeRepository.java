package com.lojister.repository.advertisement;

import com.lojister.model.entity.adminpanel.CargoType;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.model.enums.DynamicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CargoTypeRepository extends JpaRepository<CargoType,Long> {

    Set<CargoType> findByIdIn(List<Long> ids);

    List<CargoType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Set<CargoType> findByTypeNameInIgnoreCase(List<String> nameList);

    Optional<CargoType> findByTypeNameIgnoreCase(String typeName);

    @Query(value = "select  ct from #{#entityName} ct where ct.dynamicStatus =:dynamicStatus")
    Set<CargoType> getAll( @Param(value = "dynamicStatus") DynamicStatus dynamicStatus);

}
