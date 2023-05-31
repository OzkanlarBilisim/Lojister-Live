package com.lojister.repository.advertisement;

import com.lojister.model.entity.adminpanel.CargoType;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.LoadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LoadTypeRepository extends JpaRepository<LoadType,Long> {

    Set<LoadType> findByIdIn(List<Long> idList);

    List<LoadType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Set<LoadType> findByTypeNameInIgnoreCase(List<String> typeNameList);

    Optional<LoadType> findByTypeNameIgnoreCase(String typeName);

    @Query(value = "select  lt from #{#entityName} lt where lt.dynamicStatus =:dynamicStatus")
    Set<LoadType> getAll(@Param(value = "dynamicStatus") DynamicStatus dynamicStatus);
}
