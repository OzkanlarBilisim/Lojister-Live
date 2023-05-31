package com.lojister.controller.driver;

import com.lojister.controller.client.ClientDashboardResponse;
import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.client.ClientNotificationSettingDto;
import com.lojister.model.dto.driver.*;
import com.lojister.model.dto.request.ChangePasswordDto;
import com.lojister.model.dto.DriverAndDocumentListDto;
import com.lojister.model.dto.DriverStatusUpdateDto;
import com.lojister.business.abstracts.DriverService;
import com.lojister.core.api.ApiPaths;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/driver")
@CrossOrigin
@Authenticated
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getById(@PathVariable(value = "id") Long id) {

        return ResponseEntity.ok(driverService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<DriverDto>> getAll() {

        return ResponseEntity.ok(driverService.getAll());
    }

    @PostMapping()
    public ResponseEntity<DriverDto> save(@RequestBody DriverEmployeeSaveDto driverDto) {

        return ResponseEntity.ok(driverService.save(driverDto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverUpdateDto> update(@PathVariable(name = "id") Long id, @RequestBody DriverUpdateRequestDto driverDto) {

        return ResponseEntity.ok(driverService.updateDriverAndGetNewToken(id, driverDto));

    }

    @GetMapping("/status/review")
    public ResponseEntity<List<DriverAndDocumentListDto>> getReviewStatus() {

        return ResponseEntity.ok(driverService.getReviewStatus());

    }

    @GetMapping("/status/revision")
    public ResponseEntity<List<DriverAndDocumentListDto>> getRevisionStatus() {

        return ResponseEntity.ok(driverService.getRevisionStatus());

    }

    @PostMapping("/status/update")
    public void updateDriverStatus(@RequestBody DriverStatusUpdateDto driverStatusUpdateDto) {

        driverService.updateDriverStatus(driverStatusUpdateDto.getValue(), driverStatusUpdateDto.getStatusDescription(), driverStatusUpdateDto.getDriverId());

    }

    @GetMapping("/myDrivers")
    public Page<MyDriversPageDto> getMyDrivers(Pageable pageable) {
        return driverService.getMyDrivers(pageable);
    }
    @GetMapping("/myDrivers/min")
    public List<DriverMinDto> getMyDrivers() {
        return driverService.getMyDrivers();
    }

    @PostMapping("/changePassword")
    public Boolean changePassword(@RequestBody ChangePasswordDto changePasswordDto) {

        return driverService.changePassword(changePasswordDto.getOldPassword(), changePasswordDto.getNwPassword());

    }
    @GetMapping("/dashboardInformation")
    public DriverDashboardResponse getDashboardInformation(){
        return driverService.getDashboardInformation();
    }

    @PutMapping("/updateNotificationSetting")
    public DriverNotificationSettingDto updateNotificationSetting(@RequestBody UpdateDriverNotificationSettingRequest updateDriverNotificationSettingRequest){
        return driverService.updateNotificationSetting(updateDriverNotificationSettingRequest);
    }
    @GetMapping("/getNotificationSetting")
    public DriverNotificationSettingDto getNotificationSetting(){
        return  driverService.getNotificationSetting();
    }
}
