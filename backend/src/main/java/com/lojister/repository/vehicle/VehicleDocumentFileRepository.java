package com.lojister.repository.vehicle;

import com.lojister.model.dto.VehicleDocumentMinimalDto;
import com.lojister.model.dto.VehicleDocumentNotDataDto;
import com.lojister.model.entity.VehicleDocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleDocumentFileRepository extends JpaRepository<VehicleDocumentFile, Long> {

    @Query(value = "select new com.lojister.model.dto.VehicleDocumentNotDataDto(vd.id,vd.vehicle,vd.vehicleDocumentType,vd.fileName) " +
            "from " + "VehicleDocumentFile vd " + "where " + "vd.id=:id")
    VehicleDocumentNotDataDto findNoDataById(@Param(value = "id") Long id);

    List<VehicleDocumentFile> findByVehicle_Id(Long id);

    @Query(value = "select new com.lojister.model.dto.VehicleDocumentMinimalDto(vd.id,vd.vehicleDocumentType,vd.fileName) " +
            "from " + "VehicleDocumentFile vd " + "where " + "vd.vehicle.id=:vehicleId")
    List<VehicleDocumentMinimalDto> findMinimalVehicleDocumentByVehicleId(@Param(value = "vehicleId") Long vehicleId);

}
