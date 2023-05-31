package com.lojister.repository.vehicle;

import com.lojister.model.dto.MyVehiclesPageDto;
import com.lojister.model.entity.Vehicle;
import com.lojister.model.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    Long countByVehicleModelAndBrand(String vehicleModel, String vehicleBrand);

    Long countByVehicleStatusAndCompany_Id(VehicleStatus vehicleStatus,Long companyId);

    List<Vehicle> findByVehicleStatus(VehicleStatus vehicleStatus);

    List<Vehicle> findByCompany_Id(Long id);

    Optional<Vehicle> findByDriver_Id(Long id);

    @Query(value = "SELECT NEW com.lojister.model.dto.MyVehiclesPageDto(vh.id, vh.createdDateTime,vh.vehicleName,vh.licencePlate,vh.trailerPlate,vh.maxCapacity,vh.vehicleStatus,vh.statusDescription,dr.firstName,dr.lastName,pp.data,pp.fileName,vh.vehicleModel,vht.id,vht.typeName,vh.brand) " +
            "FROM Vehicle vh LEFT JOIN vh.vehicleType vht LEFT JOIN Driver dr ON dr.id = vh.driver.id LEFT JOIN ProfilePhotoFile pp ON pp.user.id = dr.id WHERE vh.company.id = :companyId")
    Page<MyVehiclesPageDto> getMyVehiclesByCompanyId(@Param(value = "companyId") Long companyId, Pageable pageable);

    @Query(value = "SELECT vh " +
            "FROM Vehicle vh LEFT JOIN vh.vehicleType vht LEFT JOIN Driver dr ON dr.id = vh.driver.id LEFT JOIN ProfilePhotoFile pp ON pp.user.id = dr.id WHERE vh.company.id = :companyId")
    List<Vehicle> getMyVehiclesByCompanyId(@Param(value = "companyId") Long companyId);

}
