package com.lojister.repository.driver;

import com.lojister.model.dto.DriverDocumentNotDataDto;
import com.lojister.model.dto.DriverDocumentMinimalDto;
import com.lojister.model.entity.driver.DriverDocumentFile;
import com.lojister.model.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DriverDocumentFileRepository extends JpaRepository<DriverDocumentFile,Long> {

    @Query(value = "select new com.lojister.model.dto.DriverDocumentNotDataDto(df.id,df.driverDocumentType, df.driver,df.fileName) " +
    "from " + "DriverDocumentFile df " + "where " +"df.id=:id")
    DriverDocumentNotDataDto findNoDataById(@Param(value = "id") Long id);

    List<DriverDocumentFile> findByDriver_Id(Long id);


    @Query(value = "select new com.lojister.model.dto.DriverDocumentNotDataDto(df.id,df.driverDocumentType, df.driver,df.fileName) " +
            "from " + "DriverDocumentFile df " + "where " +"df.driver.status=:status")
    List<DriverDocumentNotDataDto> findNoDataByStatus(@Param(value = "status") DriverStatus status);


    @Query(value = "select new com.lojister.model.dto.DriverDocumentNotDataDto(df.id,df.driverDocumentType, df.driver,df.fileName) " +
            "from " + "DriverDocumentFile df " + "where " +"df.driver.id=:driverId")
    List<DriverDocumentNotDataDto> findNoDataByDriverId(@Param(value = "driverId") Long driverId);


    @Query(value = "select new com.lojister.model.dto.DriverDocumentMinimalDto(df.id,df.driverDocumentType,df.fileName) " +
            "from " + "DriverDocumentFile df " + "where " +"df.driver.id=:driverId")
    List<DriverDocumentMinimalDto> findMinimalDriverDocumentByDriverId(@Param(value = "driverId") Long driverId );


}
