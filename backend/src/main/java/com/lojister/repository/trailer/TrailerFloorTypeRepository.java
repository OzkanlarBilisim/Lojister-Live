package com.lojister.repository.trailer;

import com.lojister.model.entity.adminpanel.TrailerFeature;
import com.lojister.model.entity.adminpanel.TrailerType;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrailerFloorTypeRepository extends JpaRepository<TrailerFloorType,Long> {

    Set<TrailerFloorType> findByIdInAndDynamicStatus(List<Long> idList,DynamicStatus dynamicStatus);

    List<TrailerFloorType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Set<TrailerFloorType> findByTypeNameInIgnoreCase(List<String> typeNameList);

    Optional<TrailerFloorType> findByTypeNameIgnoreCase(String typeName);


    @Query(value = "select  tf from #{#entityName} tf where tf.dynamicStatus =:dynamicStatus")
    Set<TrailerFloorType> getAll(@Param(value = "dynamicStatus") DynamicStatus dynamicStatus);

}
