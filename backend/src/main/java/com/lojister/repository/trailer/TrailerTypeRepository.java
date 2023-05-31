package com.lojister.repository.trailer;


import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.TrailerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrailerTypeRepository extends JpaRepository<TrailerType,Long> {

    Set<TrailerType> findByIdInAndDynamicStatus(List<Long> idList,DynamicStatus dynamicStatus);

    List<TrailerType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Set<TrailerType> findByTypeNameInIgnoreCase(List<String> typeNameList);

    Optional<TrailerType> findByTypeNameIgnoreCase(String typeName);

    @Query(value = "select  tt from #{#entityName} tt where tt.dynamicStatus =:dynamicStatus")
    Set<TrailerType> getAll(@Param(value = "dynamicStatus") DynamicStatus dynamicStatus);

}
