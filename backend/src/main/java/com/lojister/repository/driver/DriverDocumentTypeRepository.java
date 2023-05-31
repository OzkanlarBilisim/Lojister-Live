package com.lojister.repository.driver;

import com.lojister.model.entity.adminpanel.DriverDocumentType;
import com.lojister.model.enums.DynamicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverDocumentTypeRepository extends JpaRepository<DriverDocumentType,Long> {

    List<DriverDocumentType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Optional<DriverDocumentType> findByTypeNameIgnoreCase(String typeName);
}
