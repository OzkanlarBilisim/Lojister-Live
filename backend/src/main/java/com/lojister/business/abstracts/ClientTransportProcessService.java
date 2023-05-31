package com.lojister.business.abstracts;


import com.lojister.controller.document.SaveDocumentFileRequest;
import com.lojister.controller.transport.DocumentResponse;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementContainerDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessWithClientAdvertisementDto;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

public interface ClientTransportProcessService {

    ClientTransportProcessDto getById(Long id);

    void deleteById(Long id);

    ClientTransportProcess findDataByTransportCode(String transportCode);
    ClientTransportProcessDto getByTransportCode(String transportCode);
    List<ClientTransportProcessDto> getMyTransportProcesses();

    List<ClientTransportProcessDto> getAll();

    ClientTransportProcessDto findByClientAdvertisementId(Long clientAdvertisementId);

    ClientTransportProcessDto findByAcceptedClientAdvertisementBidId(Long clientAdvertisementBidId);

    ClientTransportProcessDto assignVehicle(Long clientTransportProcessId, Long vehicleId);

    void sendSms(Long clientTransportProcessId, String siteUrl);
     void containerReceived(String transportCode);
    String receivingCargoFromClient(Long clientTransportProcessId);
    void cargoDelivered(String transportCode);
    void uploadClientTransportProcessDocumentFile(FileUploadUtil.FileResult result, Long transportProcessId, SaveDocumentFileRequest saveDocumentFileRequest);
    Boolean verifyReceiveQrCode(String receiveQrCode);
    
    void assignVehicleCheck(ClientTransportProcess clientTransportProcess);
    void startTransportConfirmToken(String token);
    void endTransportConfirmToken(String token);
    Boolean acceptReceive(String transportCode);
    Boolean startTransportWithTransportCode(String transportCode);
    void startingTransportCheck(ClientTransportProcess clientTransportProcess);
    void endingTransportCheck(ClientTransportProcess clientTransportProcess);
    Boolean acceptStartingTransport(String transportCode);
    Boolean startTransport(Long clientTransportProcessId);
    Boolean endTransportWithTransportCode(String transportCode);
    String cargoDeliverToClient(Long clientTransportProcessId);

    Boolean cargoCouldNotDelivered(Long clientTransportProcessId, String explanation);

    Boolean verifyDeliverQrCode(String deliverQrCode);

    String clientCargoCompletedCheck(Long clientTransportProcessId, Boolean isSuccess, String explanation);

    String paymentCompany(Long clientTransportProcessId);
    void startingTransport(Long id) throws MessagingException, UnsupportedEncodingException;
    void endingTransport(Long id) throws MessagingException, UnsupportedEncodingException;
    ClientTransportProcess findDataById(Long id);

    ClientTransportProcess saveRepo(ClientTransportProcess clientTransportProcess);

    Page<ClientTransportProcess> findByVehicleDriverIdOrderByIdDescRepo(Long driverId, List<BidStatus> bidStatusList, Pageable pageable);

    List<ClientTransportProcess> findByVehicleIdAndTransportProcessStatusRepo(Long vehicleId, TransportProcessStatus transportProcessStatus);

    void clientUnauthorizedTransactionCheck(ClientTransportProcess clientTransportProcess);

    void driverUnauthorizedTransactionCheck(ClientTransportProcess clientTransportProcess);
    void uploadedDocumentCheck(ClientTransportProcess clientTransportProcess);
    void cargoOnTheWayCheck(ClientTransportProcess clientTransportProcess);

    ClientTransportProcess calculateTheFeeByInsuranceType(ClientTransportProcess clientTransportProcess);

    void changeStatusTrueAfterUploadingTheFile(ClientTransportProcess clientTransportProcess);

    List<ClientTransportProcessWithClientAdvertisementDto> findByDriverIdAndTransportProcessStatus(List<TransportProcessStatus> transportProcessStatusList);


    Long countByAcceptedClientAdvertisementBid_DriverBidder_IdAndCreatedDateTimeBetween(LocalDateTime start, LocalDateTime end);

    void uploadDriverTransportProcessDocumentFile(FileUploadUtil.FileResult result, Long id, SaveDocumentFileRequest saveDocumentFileRequest);

    List<DocumentResponse> getDocumentList(Long id);
}










