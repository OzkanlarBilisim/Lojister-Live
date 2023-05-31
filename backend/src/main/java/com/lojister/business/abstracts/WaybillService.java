package com.lojister.business.abstracts;

import com.lojister.model.dto.SetWaybillStatusDto;
import com.lojister.model.dto.WaybillBase64Dto;
import com.lojister.model.dto.WaybillWithoutDataDto;
import com.lojister.model.entity.Vehicle;
import com.lojister.model.entity.Waybill;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.entity.driver.Driver;
import com.lojister.core.util.FileUploadUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface WaybillService {

    void saveWaybill(MultipartFile result, Long transportProcessId);

    List<WaybillWithoutDataDto> getAllWaitingWithoutData();

    WaybillBase64Dto findBase64ById(Long id);

    Waybill findDataById(Long id);
    Optional<Waybill> findByClientTransportProcessId(Long transportProcessId);
    Waybill findDataByClientTransportProcessId(Long transportProcessId);

    void vehicleNullCheck(Vehicle vehicle);

    void driverNullCheck(Driver driver);

    void setWaybillStatus(SetWaybillStatusDto waybillStatusDto);

    void correctDriverCheck(ClientTransportProcess clientTransportProcess);

    void correctTransportProcessStatusCheck(ClientTransportProcess clientTransportProcess);

    void fileLengthCheck(long fileLength);

    void duplicateWaybillCheck(Waybill waybill);

    void waybillDeniedChangeStatus(String transportCode);

}
