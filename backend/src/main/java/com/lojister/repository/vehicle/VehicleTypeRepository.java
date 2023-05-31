package com.lojister.repository.vehicle;

import com.lojister.model.entity.adminpanel.LoadType;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType,Long> {

    Set<VehicleType> findByIdInAndDynamicStatus(List<Long> ids,DynamicStatus dynamicStatus);

    List<VehicleType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Set<VehicleType> findByTypeNameInIgnoreCase(List<String> typeNameList);

    Optional<VehicleType> findByTypeNameIgnoreCase(String typeName);

    @Query(value = "select  vt from #{#entityName} vt where vt.dynamicStatus =:dynamicStatus")
    Set<VehicleType> getAll(@Param(value = "dynamicStatus") DynamicStatus dynamicStatus);

}
