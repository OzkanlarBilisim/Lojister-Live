package com.lojister.business.concretes;

import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.business.abstracts.PushNotificationService;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.core.exception.*;
import com.lojister.model.dto.SetWaybillStatusDto;
import com.lojister.model.dto.WaybillBase64Dto;
import com.lojister.model.dto.WaybillWithoutDataDto;
import com.lojister.model.enums.*;
import com.lojister.mapper.WaybillWithoutDataMapper;
import com.lojister.model.entity.Vehicle;
import com.lojister.model.entity.Waybill;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.waybill.WaybillRepository;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.business.abstracts.WaybillService;
import com.lojister.core.helper.ContentTypeHelper;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.util.TempFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;



@Service
@Transactional
@RequiredArgsConstructor
public class WayBillServiceImpl implements WaybillService {

    private final WaybillRepository waybillRepository;
    private final ClientTransportProcessService clientTransportProcessService;
    private final SecurityContextUtil securityContextUtil;
    private final WaybillWithoutDataMapper waybillWithoutDataMapper;

    private final MailNotificationService mailNotificationService;
    private final PushNotificationService pushNotificationService;


    @Override
    public void saveWaybill(MultipartFile file, Long transportProcessId) {

        ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataById(transportProcessId);

        vehicleNullCheck(clientTransportProcess.getVehicle());
        driverNullCheck(clientTransportProcess.getVehicle().getDriver());

        correctDriverCheck(clientTransportProcess);
        correctTransportProcessStatusCheck(clientTransportProcess);

        Optional<Waybill> waybillOptional = waybillRepository.findByClientTransportProcess_Id(clientTransportProcess.getId());

        if (waybillOptional.isPresent()) {
            if (waybillOptional.get().getWaybillStatus() == WaybillStatus.APPROVED) {
                throw new WaybillStatusException(Translator.toLocale("lojister.wayBill.WaybillStatusException"));
            } else {
                waybillRepository.deleteById(waybillOptional.get().getId());
            }
        }

        try {
            Waybill waybill = new Waybill();

            long fileLength = file.getSize();
            fileLengthCheck(fileLength);

            Blob blob = BlobProxy.generateProxy(file.getInputStream(), file.getSize());
            waybill.setData(blob);
            waybill.setFileName(file.getOriginalFilename());
            waybill.setWaybillStatus(WaybillStatus.WAITING);
            waybill.setClientTransportProcess(clientTransportProcess);
            waybillRepository.save(waybill);

            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.WAITING_WAYBILL);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement()
                    .setAdvertisementProcessStatus(AdvertisementProcessStatus.WAITING_WAYBILL);
            clientTransportProcessService.saveRepo(clientTransportProcess);
        } catch (FileSizeMaxException e) {
            throw new FileSizeMaxException(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    @OnlyAdmin
    public void setWaybillStatus(SetWaybillStatusDto waybillStatusDto) {

        Waybill waybill = findDataByClientTransportProcessId(waybillStatusDto.getTransportProcessId());

        ClientTransportProcess clientTransportProcess = waybill.getClientTransportProcess();

        duplicateWaybillCheck(waybill);

        if (waybillStatusDto.getWaybillStatus() == WaybillStatus.APPROVED) {
            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.COMPLETED);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementStatus(AdvertisementStatus.FINISHED);
            clientTransportProcess.getAcceptedClientAdvertisementBid().setBidStatus(BidStatus.COMPLETED);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.COMPLETED);
            mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
            pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
            mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
            pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        } else if (waybillStatusDto.getWaybillStatus() == WaybillStatus.DENIED) {
            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.WAYBILL_DENIED);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.WAYBILL_DENIED);
        }

        waybill.setWaybillStatus(waybillStatusDto.getWaybillStatus());
        waybillRepository.save(waybill);

        clientTransportProcessService.saveRepo(clientTransportProcess);
    }


    @Override
    public void correctDriverCheck(ClientTransportProcess clientTransportProcess) {

        Long currentUserId = securityContextUtil.getCurrentUserId();

        if (!(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getId() == currentUserId ||
                clientTransportProcess.getVehicle().getDriver().getId() == currentUserId)) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.wayBill.UnauthorizedTransactionException"));
        }
    }


    @Override
    public void correctTransportProcessStatusCheck(ClientTransportProcess clientTransportProcess) {

        if (!(clientTransportProcess.getTransportProcessStatus() == TransportProcessStatus.CARGO_ON_THE_WAY ||
                clientTransportProcess.getTransportProcessStatus() == TransportProcessStatus.WAITING_WAYBILL ||
                clientTransportProcess.getTransportProcessStatus() == TransportProcessStatus.WAYBILL_DENIED)) {

            throw new TransportProcessStatusException(Translator.toLocale("lojister.wayBill.TransportProcessStatusException"));
        }
    }

    @Override
    public void fileLengthCheck(long fileLength) {

        if (fileLength > 4100000) {
            throw new FileSizeMaxException(Translator.toLocale("lojister.wayBill.FileSizeMaxException"));
        }
    }

    @Override
    public void duplicateWaybillCheck(Waybill waybill) {

        if (waybill.getWaybillStatus() == WaybillStatus.APPROVED || waybill.getWaybillStatus() == WaybillStatus.DENIED) {
            throw new WaybillStatusException(Translator.toLocale("lojister.wayBill.WaybillStatusException.process"));
        }
    }

    @Override
    public void waybillDeniedChangeStatus(String transportCode) {

        ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataByTransportCode(transportCode);

        if (clientTransportProcess.getTransportProcessStatus()==TransportProcessStatus.WAYBILL_DENIED){
            correctDriverCheck(clientTransportProcess);
            Waybill waybill = findDataByClientTransportProcessId(clientTransportProcess.getId());
            waybillRepository.delete(waybill);
            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement()
                    .setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);

            clientTransportProcessService.saveRepo(clientTransportProcess);
        }

    }


    @Override
    @OnlyAdmin
    public List<WaybillWithoutDataDto> getAllWaitingWithoutData() {

        return waybillWithoutDataMapper.entityListToDtoList(waybillRepository.findNotDataByWaybillStatus(WaybillStatus.WAITING));
    }


    @Override
    @OnlyAdmin
    public WaybillBase64Dto findBase64ById(Long id) {

        Waybill waybill = findDataById(id);

        WaybillBase64Dto waybillBase64Dto = new WaybillBase64Dto();

        String ext = FilenameUtils.getExtension(waybill.getFileName());
        String contentType = ContentTypeHelper.getDataByContentType(ext);

        try {

            InputStream is = waybill.getData().getBinaryStream();
            byte[] bytes = IOUtils.toByteArray(is);
            String encoded = Base64.encodeBase64String(bytes);
            waybillBase64Dto.setData(encoded);
            waybillBase64Dto.setContentType(contentType);
            waybillBase64Dto.setWaybillStatus(waybill.getWaybillStatus());

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return waybillBase64Dto;
    }

    @Override
    public Waybill findDataById(Long id) {

        Optional<Waybill> waybillOptional = waybillRepository.findById(id);

        if (waybillOptional.isPresent()) {
            return waybillOptional.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.wayBill.EntityNotFoundException"));
        }
    }

    @Override
    public Optional<Waybill> findByClientTransportProcessId(Long transportProcessId) {
        return waybillRepository.findByClientTransportProcess_Id(transportProcessId);
    }

    @Override
    public Waybill findDataByClientTransportProcessId(Long transportProcessId) {

        Optional<Waybill> waybillOptional = waybillRepository.findByClientTransportProcess_Id(transportProcessId);

        if (waybillOptional.isPresent()) {
            return waybillOptional.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.wayBill.EntityNotFoundException.photo"));
        }
    }

    @Override
    public void vehicleNullCheck(Vehicle vehicle) {

        if (vehicle == null) {
            throw new EntityNotFoundException(Translator.toLocale("lojister.wayBill.EntityNotFoundException.vehicle"));
        }
    }

    @Override
    public void driverNullCheck(Driver driver) {

        if (driver == null) {
            throw new EntityNotFoundException(Translator.toLocale("lojister.wayBill.EntityNotFoundException.driver"));
        }
    }


}
