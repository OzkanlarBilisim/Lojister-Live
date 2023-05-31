package com.lojister.repository.vehicle;

import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleDocumentTypeRepository extends JpaRepository<VehicleDocumentType,Long> {

    List<VehicleDocumentType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Optional<VehicleDocumentType> findByTypeNameIgnoreCase(String typeName);
}
