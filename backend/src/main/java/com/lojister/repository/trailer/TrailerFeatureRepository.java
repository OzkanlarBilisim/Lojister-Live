package com.lojister.repository.trailer;

import com.lojister.model.entity.adminpanel.VehicleType;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.TrailerFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrailerFeatureRepository extends JpaRepository<TrailerFeature,Long> {

    Set<TrailerFeature> findByIdIn(List<Long> idList);

    List<TrailerFeature> findByDynamicStatus(DynamicStatus dynamicStatus);

    Set<TrailerFeature> findByFeatureNameInIgnoreCase(List<String> featureNameList);

    Optional<TrailerFeature> findByFeatureNameIgnoreCase(String featureName);


    @Query(value = "select  tf from #{#entityName} tf where tf.dynamicStatus =:dynamicStatus")
    Set<TrailerFeature> getAll(@Param(value = "dynamicStatus") DynamicStatus dynamicStatus);
}
