package com.lojister.business.abstracts;

import com.lojister.model.dto.*;
import com.lojister.model.entity.Vehicle;
import com.lojister.model.entity.driver.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService extends BaseService<VehicleDto> {

    List<VehicleAndDocumentListDto> getReviewStatus();

    List<VehicleAndDocumentListDto> getRevisionStatus();

    void updateLastLocation(PositionDto positionDto);

    LastPositionResponseDto getDriverLastPositionByTransportProcessId(Long transportProcessId);

    Boolean unAssignDriverFromVehicle(Long vehicleId);

    Page<MyVehiclesPageDto> getMyVehicles(Pageable pageable);
    List<VehicleMinDto> getMyVehicles();

    void updateVehicleStatus(Boolean value, String statusDescription, Long vehicleId);

    VehicleDto changeDriver(Long vehicleId, Long driverId,Long transportProcessId);

    Vehicle findDataById(Long vehicleId);

    Vehicle saveRepo(Vehicle vehicle);

    List<VehicleAndDocumentListDto> getVehicleAndDocumentListDtoList(List<Vehicle> vehicleList);

    void vehicleAssignedToDriverCheck(Driver driver);
    void vehicleAcceptedCheck(Vehicle vehicle);
    void unAssignDriverFromVehiclePermissionCheck(Vehicle vehicle, Driver boss);

    void vehicleOnTheRoadCheck(Vehicle vehicle);

    void driverNullCheck(Vehicle vehicle);

}
