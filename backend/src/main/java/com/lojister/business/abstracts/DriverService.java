package com.lojister.business.abstracts;


import com.lojister.controller.client.ClientDashboardResponse;
import com.lojister.controller.driver.DriverDashboardResponse;
import com.lojister.controller.driver.UpdateDriverNotificationSettingRequest;
import com.lojister.model.dto.DriverAndDocumentListDto;
import com.lojister.model.dto.client.ClientNotificationSettingDto;
import com.lojister.model.dto.driver.*;
import com.lojister.model.entity.driver.DriverNotificationSetting;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverService {

    DriverDto save(DriverEmployeeSaveDto dto);

    DriverUpdateDto updateDriverAndGetNewToken(Long id, DriverUpdateRequestDto driverDto);

    DriverDto getById(Long id);

    void deleteById(Long id);

    List<DriverAndDocumentListDto> getReviewStatus();

    List<DriverAndDocumentListDto> getRevisionStatus();

    List<DriverDto> getAll();

    Page<MyDriversPageDto> getMyDrivers(Pageable pageable);
    List<DriverMinDto> getMyDrivers();

    void updateDriverStatus(Boolean value, String statusDescription, Long driverId);

    Boolean changePassword(String oldPassword, String nwPassword);

    DriverUpdateDto createDriverUpdateDto(Driver driver);

    Driver findDataById(Long id);

    Driver updateDriverWithTokenSaveDriver(Driver driver, DriverUpdateRequestDto driverDto);

    Driver saveRepo(Driver driver);

    void unauthorizedTransactionCheck(Driver driver, Driver currentDriver);

    void updateDriverEmailCheck(Driver driver, DriverUpdateRequestDto driverDto);

    void updateDriverPhoneCheck(Driver driver, DriverUpdateRequestDto driverDto);

    List<DriverAndDocumentListDto> getDriverAndDocumentListDtoList(List<Driver> driverList);

    Driver findByCompanyIdAndDriverTitle(Long companyId, DriverTitle driverTitle);

    List<String> getAllFirebaseTokenFromDrivers();

    DriverDashboardResponse getDashboardInformation();

    DriverNotificationSettingDto updateNotificationSetting(UpdateDriverNotificationSettingRequest updateDriverNotificationSettingRequest);

    DriverNotificationSettingDto getNotificationSetting();

    List<Driver> findByDriverTitleIn(List<DriverTitle> driverTitleList);

}
