package com.lojister.repository.advertisement;

import com.lojister.model.enums.DynamicStatus;
import com.lojister.model.entity.adminpanel.PackagingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackagingTypeRepository extends JpaRepository<PackagingType,Long> {

    List<PackagingType> findByDynamicStatus(DynamicStatus dynamicStatus);

    Optional<PackagingType> findByTypeName(String typeName);

    Optional<PackagingType> findByTypeNameIgnoreCase(String typeName);
}
