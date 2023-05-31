package com.lojister.controller.vehicle;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.business.abstracts.VehicleService;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.*;
import com.lojister.core.api.ApiPaths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/vehicle")
@CrossOrigin
@Authenticated
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(vehicleService.getById(id));

    }

    @GetMapping()
    public ResponseEntity<List<VehicleDto>> getAll() {

        return ResponseEntity.ok(vehicleService.getAll());
    }

    @PostMapping()
    public ResponseEntity<VehicleDto> save(@Valid @RequestBody VehicleDto vehicleDto) {

        return ResponseEntity.ok(vehicleService.save(vehicleDto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> update(@PathVariable(name = "id") Long id, @RequestBody VehicleDto vehicleDto) {

        return ResponseEntity.ok(vehicleService.update(id, vehicleDto));
    }

    @PutMapping("/{vehicleId}/changeDriver")
    public ResponseEntity<VehicleDto> changeDriver(@PathVariable(name = "vehicleId") Long vehicleId, @RequestParam(name = "driverId") Long driverId,@RequestParam Long transportProcessId) {

        return ResponseEntity.ok(vehicleService.changeDriver(vehicleId, driverId, transportProcessId));
    }

    @GetMapping("/{vehicleId}/unAssignDriver")
    public ResponseEntity<Boolean> unAssignDriverFromVehicle(@PathVariable(value = "vehicleId") Long vehicleId) {

        return ResponseEntity.ok(vehicleService.unAssignDriverFromVehicle(vehicleId));
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        vehicleService.deleteById(id);
    }

    @GetMapping("/status/review")
    public ResponseEntity<List<VehicleAndDocumentListDto>> getReviewStatus() {

        return ResponseEntity.ok(vehicleService.getReviewStatus());
    }

    @GetMapping("/status/revision")
    public ResponseEntity<List<VehicleAndDocumentListDto>> getRevisionStatus() {

        return ResponseEntity.ok(vehicleService.getRevisionStatus());
    }

    @PostMapping("/status/update")
    public void updateVehicleStatus(@RequestBody VehicleStatusUpdateDto vehicleStatusUpdateDto) {

        vehicleService.updateVehicleStatus(vehicleStatusUpdateDto.getValue(), vehicleStatusUpdateDto.getStatusDescription(), vehicleStatusUpdateDto.getVehicleId());
    }

    @GetMapping("/myVehicles")
    public Page<MyVehiclesPageDto> getMyVehicles(Pageable pageable) {

        return vehicleService.getMyVehicles(pageable);

    }
    @GetMapping("/myVehicles/min")
    public List<VehicleMinDto> getMyVehicles() {
        return vehicleService.getMyVehicles();
    }

    @PutMapping("/lastLocation")
    public void updateLastLocation(@RequestBody PositionDto positionDto) {

        vehicleService.updateLastLocation(positionDto);
    }

    @PermitAllCustom
    @GetMapping("/transportProcess/{transportProcessId}")
    public ResponseEntity<LastPositionResponseDto> getDriverLastPositionByTransportProcessId(@PathVariable(value = "transportProcessId") Long transportProcessId) {

        return ResponseEntity.ok(vehicleService.getDriverLastPositionByTransportProcessId(transportProcessId));
    }


}
