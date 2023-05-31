package com.lojister.business.concretes;

import com.lojister.controller.document.SaveDocumentFileRequest;
import com.lojister.controller.transport.DocumentResponse;
import com.lojister.core.exception.*;
import com.lojister.core.i18n.Translator;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.TempFileUtil;
import com.lojister.core.util.sender.ConfirmationTokenMailSender;
import com.lojister.mapper.ClientTransportProcessWithClientAdvertisementDtoMapper;
import com.lojister.model.dto.DocumentFileWithoutDataDto;
import com.lojister.model.dto.SendMailConfirmationTransportDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementContainerDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessWithClientAdvertisementDto;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import com.lojister.model.entity.client.ClientAdvertisementContainer;
import com.lojister.model.entity.driver.DriverDocumentFile;
import com.lojister.model.enums.*;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.business.abstracts.*;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.mapper.ClientTransportProcessMapper;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.*;
import com.lojister.repository.transport.ClientTransportProcessRepository;
import com.lojister.core.util.generator.RandomStringGenerator;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.service.vatansms.VatanSmsService;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ClientTransportProcessServiceImpl implements ClientTransportProcessService {

    private final ClientTransportProcessRepository clientTransportProcessRepository;
    private final ClientAdvertisementService clientAdvertisementService;
    private final TransportPaymentService transportPaymentService;
    private final ClientAdvertisementBidService clientAdvertisementBidService;
    private final VehicleService vehicleService;
    private final ClientTransportProcessMapper clientTransportProcessMapper;
    private final SecurityContextUtil securityContextUtil;
    private final RandomStringGenerator randomStringGenerator;
    private final VatanSmsService vatanSmsService;
    private final TransportProcessService transportProcessService;
    private final ClientTransportProcessWithClientAdvertisementDtoMapper clientTransportProcessWithClientAdvertisementDtoMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final ClientAdvertisementContainerService clientAdvertisementContainerService;
    private final ConfirmationTokenMailSender confirmationTokenMailSender;
    private final MailNotificationService mailNotificationService;
    private final PushNotificationService pushNotificationService;
    private final DocumentService documentService;
    @Lazy
    private final WaybillService waybillService;

    private static final String CLIENT_UNAUTHORIZED_TRANSACTION_MESSAGE = "Yanlış müşteri işlem yapmaktadır.";
    private static final String CLIENT_UNAUTHORIZED_TRANSACTION_MESSAGE_EN = "The wrong customer is making a transaction.";
    private static final String DRIVER_UNAUTHORIZED_TRANSACTION_MESSAGE = "Yanlış sürücü işlem yapmaktadır.";
    private static final String DRIVER_UNAUTHORIZED_TRANSACTION_MESSAGE_EN = "The wrong driver is making a transaction";

    private static final double NARROW_INSURANCE_RATIO = 0.0007;
    private static final double COMPREHENSIVE_INSURANCE_RATIO = 0.0007;

    @Value("${lojister.api.url}")
    private String apiUrl;

    @Override
    public ClientTransportProcess findDataById(Long id) {
        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findById(id);

        if (clientTransportProcess.isPresent()) {

            return clientTransportProcess.get();

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.clientTransportProcess.EntityNotFoundException.cargo"));
        }
    }

    @Override
    public ClientTransportProcess saveRepo(ClientTransportProcess clientTransportProcess) {
        return clientTransportProcessRepository.save(clientTransportProcess);
    }

    @Override
    public Page<ClientTransportProcess> findByVehicleDriverIdOrderByIdDescRepo(Long driverId, List<BidStatus> bidStatusList, Pageable pageable) {
        return clientTransportProcessRepository.findByVehicle_Driver_IdAndAcceptedClientAdvertisementBid_BidStatusIn(driverId, bidStatusList, pageable);
    }

    @Override
    public List<ClientTransportProcess> findByVehicleIdAndTransportProcessStatusRepo(Long vehicleId, TransportProcessStatus transportProcessStatus) {
        return clientTransportProcessRepository.findByVehicle_IdAndTransportProcessStatus(vehicleId, transportProcessStatus);
    }

    @Override
    public void clientUnauthorizedTransactionCheck(ClientTransportProcess clientTransportProcess) {

        if (clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getId() != securityContextUtil.getCurrentClient().getId()) {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.clientTransportProcess.UnauthorizedTransactionException.client"));
        }
    }

    @Override
    public void driverUnauthorizedTransactionCheck(ClientTransportProcess clientTransportProcess) {

        if (clientTransportProcess.getVehicle().getDriver().getBoss().getId() != securityContextUtil.getCurrentDriver().getId()) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.clientTransportProcess.UnauthorizedTransactionException.driver"));
        }

    }

    @Override
    public void uploadedDocumentCheck(ClientTransportProcess clientTransportProcess) {
        if (clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getAdvertisementProcessStatus()!=AdvertisementProcessStatus.UPLOADED_DOCUMENT){
            throw new WrongStepException(Translator.toLocale("lojister.clientTransportProcess.WrongStepException.file"));
        }
    }

    @Override
    public void cargoOnTheWayCheck(ClientTransportProcess clientTransportProcess) {
        if (clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getAdvertisementProcessStatus()!=AdvertisementProcessStatus.CARGO_ON_THE_WAY){
            throw new WrongStepException(Translator.toLocale("lojister.clientTransportProcess.WrongStepException.transport"));
        }
    }


    @Override
    public ClientTransportProcessDto getById(Long id) {

        return clientTransportProcessMapper.entityToDto(findDataById(id));
    }

    @Override
    public void deleteById(Long id) {

        findDataById(id);
        clientTransportProcessRepository.deleteById(id);
    }

    @Override
    public ClientTransportProcess findDataByTransportCode(String transportCode) {

        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByTransportCode(transportCode);

        if (clientTransportProcess.isPresent()) {
            return clientTransportProcess.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.clientTransportProcess.EntityNotFoundException.transport"));
        }
    }

    @Override
    public ClientTransportProcessDto getByTransportCode(String transportCode) {
        return clientTransportProcessMapper.entityToDto(findDataByTransportCode(transportCode));
    }

    @Override
    public void containerReceived(String transportCode) {
        ClientTransportProcess clientTransportProcess = findDataByTransportCode(transportCode);
        uploadedDocumentCheck(clientTransportProcess);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CONTAINER_RECEIVED);
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CONTAINER_RECEIVED);
        clientTransportProcessRepository.save(clientTransportProcess);
    }
    @Override
    public void cargoDelivered(String transportCode) {
        ClientTransportProcess clientTransportProcess = findDataByTransportCode(transportCode);
        cargoOnTheWayCheck(clientTransportProcess);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_DELIVERED);
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CARGO_DELIVERED);
        clientTransportProcessRepository.save(clientTransportProcess);
    }

    @Override
    public List<ClientTransportProcessDto> getMyTransportProcesses() {

        Role role = securityContextUtil.getCurrentUserRole();

        if (role == Role.ROLE_CLIENT) {

            return clientTransportProcessMapper.entityListToDtoList(clientTransportProcessRepository.findClientTransportProcessByClient_Id(securityContextUtil.getCurrentClient().getId()));

        } else if (role == Role.ROLE_DRIVER) {

            return clientTransportProcessMapper.entityListToDtoList(clientTransportProcessRepository.findClientTransportProcessByDriver_Id(securityContextUtil.getCurrentDriver().getId()));

        } else if (role == Role.ROLE_DRIVER_EMPLOYEE) {

            return clientTransportProcessMapper.entityListToDtoList(clientTransportProcessRepository.findByVehicle_Driver_IdOrderByIdDesc(securityContextUtil.getCurrentDriver().getId()));
        } else {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.clientTransportProcess.UnauthorizedTransactionException.role"));
        }
    }

    @Override
    @OnlyAdmin
    public List<ClientTransportProcessDto> getAll() {

        return clientTransportProcessMapper.entityListToDtoList(clientTransportProcessRepository.findAll());
    }

    @Override
    public ClientTransportProcessDto findByClientAdvertisementId(Long clientAdvertisementId) {
        return clientTransportProcessMapper.entityToDto(clientTransportProcessRepository.findClientTransportProcessByClientAdvertisement_Id(clientAdvertisementId));
    }

    @Override
    public ClientTransportProcessDto findByAcceptedClientAdvertisementBidId(Long clientAdvertisementBidId) {

        return clientTransportProcessMapper.entityToDto(clientTransportProcessRepository.findByAcceptedClientAdvertisementBid_Id(clientAdvertisementBidId));
    }

    @Override
    public ClientTransportProcessDto assignVehicle(Long clientTransportProcessId, Long vehicleId) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        if (!(clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getId().equals(securityContextUtil.getCurrentDriver().getId()))) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.clientTransportProcess.UnauthorizedTransactionException.process"));
        }

        Vehicle vehicle = vehicleService.findDataById(vehicleId);

        if (vehicle.getCompany() != clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany()) {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.clientTransportProcess.UnauthorizedTransactionException.NotSomePerson"));
        }

        transportProcessService.paymentSuccessfulCheck(clientTransportProcess);

        clientTransportProcess.setVehicle(vehicle);

        //todo
        SummaryVehicleData summaryVehicleData = new SummaryVehicleData();
        summaryVehicleData.setVehicleModel(vehicle.getVehicleModel());
        summaryVehicleData.setBrand(vehicle.getBrand());
        summaryVehicleData.setLicencePlate(vehicle.getLicencePlate());
        summaryVehicleData.setTrailerPlate(vehicle.getTrailerPlate());
        summaryVehicleData.setCompanyName(vehicle.getCompany().getCommercialTitle());

        // vehicleService.driverNullCheck(vehicle);
        vehicleService.vehicleAcceptedCheck(vehicle);
        //todo
        if (Optional.ofNullable(vehicle.getDriver()).isPresent()) {
            SummaryDriverData summaryDriverData = new SummaryDriverData();
            summaryDriverData.setCitizenId(vehicle.getDriver().getCitizenId());
            summaryDriverData.setCompanyName(vehicle.getDriver().getCompany().getCommercialTitle());
            summaryDriverData.setFirstName(vehicle.getDriver().getFirstName());
            summaryDriverData.setLastName(vehicle.getDriver().getLastName());
            summaryDriverData.setMail(vehicle.getDriver().getEmail());
            summaryDriverData.setPhone(vehicle.getDriver().getPhone());
            clientTransportProcess.setSummaryDriverData(summaryDriverData);
        }

        clientTransportProcess.setSummaryVehicleData(summaryVehicleData);

        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.ASSIGN_VEHICLE);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.ASSIGNED_VEHICLE);
        ClientTransportProcess savedClientTransportProcess = clientTransportProcessRepository.save(clientTransportProcess);
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        return clientTransportProcessMapper.entityToDto(savedClientTransportProcess);
    }


    @Override
    public void sendSms(Long clientTransportProcessId, String siteUrl) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        clientUnauthorizedTransactionCheck(clientTransportProcess);

        if (clientTransportProcess.getTransportProcessStatus() == TransportProcessStatus.ASSIGN_VEHICLE) {

            String qrCodeUrl = siteUrl + "/app/qrCode?code=" + clientTransportProcess.getReceiveQrCode();

            vatanSmsService.sendSms(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartRecipient().getPhoneNumber(),
                    clientTransportProcess.getReceiveQrCode(),
                    qrCodeUrl,
                    clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartRecipient().getFirstName(),
                    clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getStartRecipient().getLastName(),
                    "START");

        } else if (clientTransportProcess.getTransportProcessStatus() == TransportProcessStatus.CARGO_ON_THE_WAY) {

            String qrCodeUrl = siteUrl + "/app/qrCode?code=" + clientTransportProcess.getDeliverQrCode();

            vatanSmsService.sendSms(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueRecipient().getPhoneNumber(),
                    clientTransportProcess.getDeliverQrCode(),
                    qrCodeUrl,
                    clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueRecipient().getFirstName(),
                    clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDueRecipient().getLastName(),
                    "FINISH");
        } else {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException"));
        }
    }


    //Client kargoyu yollamak için teslim ederken qr kod oluşturacak. Bunun için client tarafında ödemenin yapılmış olması lazım.
    @Override
    public String receivingCargoFromClient(Long clientTransportProcessId) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        clientUnauthorizedTransactionCheck(clientTransportProcess);
        transportProcessService.assignVehicleCheck(clientTransportProcess);

        clientTransportProcess.setReceiveQrCode(randomStringGenerator.generateString());
        clientTransportProcessRepository.save(clientTransportProcess);

        return clientTransportProcess.getReceiveQrCode();
    }

    @Override
    public void uploadClientTransportProcessDocumentFile(FileUploadUtil.FileResult result, Long transportProcessId, SaveDocumentFileRequest saveDocumentFileRequest) {
        DocumentFile documentFile = new DocumentFile();

        User user = securityContextUtil.getCurrentClient();
        ClientTransportProcess clientTransportProcess = findDataById(transportProcessId);
        assignVehicleCheck(clientTransportProcess);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.UPLOADED_DOCUMENT);
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.UPLOADED_DOCUMENT);
        try (TempFileUtil.TempFileResult tempFileResult = TempFileUtil.cache(result.getFilename(), result.getFileStream())) {
            Blob blob = BlobProxy.generateProxy(tempFileResult.getFileInputStream(), tempFileResult.getLength());
            documentFile.setData(blob);
            documentFile.setFileName(result.getFilename());
            documentFile.setDocumentType(saveDocumentFileRequest.getDocumentType());
            documentFile.setUser(user);
            documentFile.setClientTransportProcess(clientTransportProcess);
            documentService.save(documentFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Driver, Client'ın oluşturduğu qrCode'u verify ediyor.  Bunun için müşteri tarafında ödemenin yapılmış olması lazım.
    @Override
    public Boolean verifyReceiveQrCode(String receiveQrCode) {

        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByReceiveQrCode(receiveQrCode);

        if (!clientTransportProcess.isPresent()) {
            throw new WrongQrCodeException(Translator.toLocale("lojister.clientTransportProcess.WrongQrCodeException"));
        }

        driverUnauthorizedTransactionCheck(clientTransportProcess.get());

        transportProcessService.assignVehicleCheck(clientTransportProcess.get());

        clientTransportProcess.get().setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);
        clientTransportProcess.get().getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
        ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.get().getAcceptedClientAdvertisementBid();
        clientAdvertisementBid.setBidStatus(BidStatus.TRANSPORT);
        clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

        clientTransportProcess.get().setAcceptedClientAdvertisementBid(clientAdvertisementBid);
        clientTransportProcess.get().setAdmissionDate(LocalDate.now());
        clientTransportProcess.get().setAdmissionTime(LocalTime.now());

        clientTransportProcessRepository.save(clientTransportProcess.get());
        return Boolean.TRUE;
    }

    @Override
    public void assignVehicleCheck(ClientTransportProcess clientTransportProcess) {
        if (clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getAdvertisementProcessStatus() != AdvertisementProcessStatus.ASSIGNED_VEHICLE) {
            throw new WrongStepException(Translator.toLocale("lojister.clientTransportProcess.WrongStepException.vehicle"));
        }
    }

    @Override
    public void startTransportConfirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);

        ClientTransportProcess clientTransportProcess = confirmationToken.getClientTransportProcess();
        startingTransportCheck(clientTransportProcess);
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);

        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
        ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.getAcceptedClientAdvertisementBid();
        clientAdvertisementBid.setBidStatus(BidStatus.TRANSPORT);
        clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

        clientTransportProcess.setAcceptedClientAdvertisementBid(clientAdvertisementBid);
        clientTransportProcess.setAdmissionDate(LocalDate.now());
        clientTransportProcess.setAdmissionTime(LocalTime.now());
        ClientTransportProcess savedClientTransportProcess = clientTransportProcessRepository.save(clientTransportProcess);
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        confirmationTokenService.delete(confirmationToken.getId());
    }

    @Override
    public void endTransportConfirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        ClientTransportProcess clientTransportProcess = confirmationToken.getClientTransportProcess();
        endingTransportCheck(clientTransportProcess);
        Optional<Waybill> waybill = waybillService.findByClientTransportProcessId(clientTransportProcess.getId());
        if (waybill.isPresent()) {
            if (waybill.get().getWaybillStatus() == WaybillStatus.APPROVED || waybill.get().getWaybillStatus() == WaybillStatus.DENIED) {
                throw new CannotBeProcessedException(Translator.toLocale("lojister.clientTransportProcess.CannotBeProcessedException"));
            }
        }
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.COMPLETED);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementStatus(AdvertisementStatus.FINISHED);
        clientTransportProcess.getAcceptedClientAdvertisementBid().setBidStatus(BidStatus.COMPLETED);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.COMPLETED);

        clientTransportProcessRepository.save(clientTransportProcess);
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
    }

    @Override
    public Boolean acceptReceive(String transportCode) {
        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByTransportCode(transportCode);

        if (!clientTransportProcess.isPresent()) {
            throw new WrongTransportProcessCode(Translator.toLocale("lojister.clientTransportProcess.WrongTransportProcessCode"));
        }
        if (clientTransportProcess.get().getVehicle().getDriver().getRole() == Role.ROLE_DRIVER_EMPLOYEE) {
            driverUnauthorizedTransactionCheck(clientTransportProcess.get());
        }

        transportProcessService.assignVehicleCheck(clientTransportProcess.get());

        clientTransportProcess.get().setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);
        clientTransportProcess.get().getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
        ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.get().getAcceptedClientAdvertisementBid();
        clientAdvertisementBid.setBidStatus(BidStatus.TRANSPORT);
        clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

        clientTransportProcess.get().setAcceptedClientAdvertisementBid(clientAdvertisementBid);
        clientTransportProcess.get().setAdmissionDate(LocalDate.now());
        clientTransportProcess.get().setAdmissionTime(LocalTime.now());
        ClientTransportProcess savedClientTransportProcess = clientTransportProcessRepository.save(clientTransportProcess.get());
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        return Boolean.TRUE;
    }

    @Override
    public Boolean startTransportWithTransportCode(String transportCode) {
        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByTransportCode(transportCode);

        if (!clientTransportProcess.isPresent()) {
            throw new WrongTransportProcessCode(Translator.toLocale("lojister.clientTransportProcess.WrongTransportProcessCode"));
        }
        startingTransportCheck(clientTransportProcess.get());
        clientUnauthorizedTransactionCheck(clientTransportProcess.get());
        clientTransportProcess.get().setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);
        clientTransportProcess.get().getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
        ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.get().getAcceptedClientAdvertisementBid();
        clientAdvertisementBid.setBidStatus(BidStatus.TRANSPORT);
        clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

        clientTransportProcess.get().setAcceptedClientAdvertisementBid(clientAdvertisementBid);
        clientTransportProcess.get().setAdmissionDate(LocalDate.now());
        clientTransportProcess.get().setAdmissionTime(LocalTime.now());
        ClientTransportProcess savedClientTransportProcess = clientTransportProcessRepository.save(clientTransportProcess.get());
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(savedClientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());

        return Boolean.TRUE;
    }

    @Override
    public void startingTransportCheck(ClientTransportProcess clientTransportProcess) {
        if (clientTransportProcess.getTransportProcessStatus() != TransportProcessStatus.STARTING_TRANSPORT) {
            throw new WrongStepException(Translator.toLocale("lojister.clientTransportProcess.WrongStepException.acceptedAdvertisement"));
        }
    }

    @Override
    public void endingTransportCheck(ClientTransportProcess clientTransportProcess) {
        if (clientTransportProcess.getTransportProcessStatus() != TransportProcessStatus.ENDING_TRANSPORT) {
            throw new WrongStepException(Translator.toLocale("lojister.clientTransportProcess.WrongStepException.acceptedAdvertisement"));
        }
    }

    @Override
    public Boolean acceptStartingTransport(String transportCode) {
        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByTransportCode(transportCode);

        if (!clientTransportProcess.isPresent()) {
            throw new WrongTransportProcessCode(Translator.toLocale("lojister.clientTransportProcess.WrongTransportProcessCode"));
        }
        if (clientTransportProcess.get().getVehicle().getDriver().getRole() == Role.ROLE_DRIVER_EMPLOYEE) {
            driverUnauthorizedTransactionCheck(clientTransportProcess.get());
        }

        transportProcessService.assignVehicleCheck(clientTransportProcess.get());

        clientTransportProcess.get().setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);
        clientTransportProcess.get().getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
        ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.get().getAcceptedClientAdvertisementBid();
        clientAdvertisementBid.setBidStatus(BidStatus.TRANSPORT);
        clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

        clientTransportProcess.get().setAcceptedClientAdvertisementBid(clientAdvertisementBid);
        clientTransportProcess.get().setAdmissionDate(LocalDate.now());
        clientTransportProcess.get().setAdmissionTime(LocalTime.now());
        clientTransportProcessRepository.save(clientTransportProcess.get());
        return Boolean.TRUE;
    }

    //Driver yolculuğa başladığı esnada buraya istek atıyor ki transport statüsü yolda olarak güncellensin.
    @Override
    public Boolean startTransport(Long clientTransportProcessId) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        driverUnauthorizedTransactionCheck(clientTransportProcess);

        if (clientTransportProcess.getTransportProcessStatus() == TransportProcessStatus.APPROVED) {

            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CARGO_ON_THE_WAY);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_ON_THE_WAY);
            clientTransportProcessRepository.save(clientTransportProcess);
            return Boolean.TRUE;

        } else {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.qr"));
        }
    }

    @Override
    public Boolean endTransportWithTransportCode(String transportCode) {
        ClientTransportProcess clientTransportProcess = findDataByTransportCode(transportCode);
        clientUnauthorizedTransactionCheck(clientTransportProcess);
        endingTransportCheck(clientTransportProcess);
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.COMPLETED);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.COMPLETED);
        String message = paymentCompany(clientTransportProcess.getId());

        clientTransportProcess.setReceiveQrCode(null);
        clientTransportProcess.setDeliverQrCode(null);

        ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.getAcceptedClientAdvertisementBid();

        ClientAdvertisement clientAdvertisement = clientAdvertisementBid.getClientAdvertisement();

        clientAdvertisement.setAdvertisementStatus(AdvertisementStatus.FINISHED);

        clientAdvertisement = clientAdvertisementService.saveRepo(clientAdvertisement);

        clientAdvertisementBid.setClientAdvertisement(clientAdvertisement);
        clientAdvertisementBid.setBidStatus(BidStatus.COMPLETED);

        clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

        clientTransportProcess.setAcceptedClientAdvertisementBid(clientAdvertisementBid);

        clientTransportProcessRepository.save(clientTransportProcess);
        mailNotificationService.statusChangeClientAdvertisementSendMailToClient(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToClient(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        mailNotificationService.statusChangeClientAdvertisementSendMailToDriver(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        pushNotificationService.statusChangeClientAdvertisementSendMobileToDriver(clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement());
        return Boolean.TRUE;

    }


    //Client Kargoyu Teslim Alacagi Sırada Bu İsteği Atar ve Qr Code Oluşturur.
    @Override
    public String cargoDeliverToClient(Long clientTransportProcessId) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        clientUnauthorizedTransactionCheck(clientTransportProcess);
        transportProcessService.cargoOnTheWayCheck(clientTransportProcess, Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"), Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"));

        clientTransportProcess.setDeliverQrCode(randomStringGenerator.generateString());
        clientTransportProcessRepository.save(clientTransportProcess);

        return clientTransportProcess.getDeliverQrCode();
    }


    //Driver Çeşitli Sebeplerden Dolayı Kargoyu Teslim Edemediği Zaman Buraya İstek Atar ve Açıklamasını Da Yollar.
    @Override
    public Boolean cargoCouldNotDelivered(Long clientTransportProcessId, String explanation) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        driverUnauthorizedTransactionCheck(clientTransportProcess);

        transportProcessService.cargoOnTheWayCheck(clientTransportProcess, Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"), Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"));

        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CARGO_COULD_NOT_BE_DELIVERED);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CARGO_COULD_NOT_BE_DELIVERED);
        clientTransportProcess.setDriverExplanation(explanation);
        clientTransportProcessRepository.save(clientTransportProcess);
        return Boolean.TRUE;
    }


    //Driver, kargoyu Client'a teslim ettiği zaman Qr Code'u Verify Eder.
    @Override
    public Boolean verifyDeliverQrCode(String deliverQrCode) {

        Optional<ClientTransportProcess> clientTransportProcess = clientTransportProcessRepository.findByDeliverQrCode(deliverQrCode);


        if (!(clientTransportProcess.isPresent())) {
            throw new WrongQrCodeException(Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.qr"));
        }

        driverUnauthorizedTransactionCheck(clientTransportProcess.get());

        transportProcessService.cargoOnTheWayCheck(clientTransportProcess.get(), Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"), Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"));

        clientTransportProcess.get().setTransportProcessStatus(TransportProcessStatus.CARGO_WAS_DELIVERED);
        clientTransportProcess.get().setDeliveryDate(LocalDate.now());

        clientTransportProcessRepository.save(clientTransportProcess.get());
        return Boolean.TRUE;
    }


    // Müşteri Teslim Aldığı Kargonun Sağlamlığına Bakar ve Buraya İstek Atar. Eğer Kırık Dökük Bir Şey Varsa İşlemi Onaylamaz ve Açıklamasını Yazarak İstek Atar.
    // Eğer Başarılı Bir Şekilde Sonuçlanırsa Şirketin Banka Hesabına Para Aktarma Fonksiyonuna Yönlendirilir.
    @Override
    public String clientCargoCompletedCheck(Long clientTransportProcessId, Boolean isSuccess, String explanation) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);
        clientUnauthorizedTransactionCheck(clientTransportProcess);
        transportProcessService.cargoWasDeliveredCheck(clientTransportProcess);


        if (isSuccess) {

            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.COMPLETED);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.COMPLETED);
            clientTransportProcess.setClientExplanation(explanation);

            String message = paymentCompany(clientTransportProcess.getId());

            clientTransportProcess.setReceiveQrCode(null);
            clientTransportProcess.setDeliverQrCode(null);

            ClientAdvertisementBid clientAdvertisementBid = clientTransportProcess.getAcceptedClientAdvertisementBid();

            ClientAdvertisement clientAdvertisement = clientAdvertisementBid.getClientAdvertisement();

            clientAdvertisement.setAdvertisementStatus(AdvertisementStatus.FINISHED);

            clientAdvertisement = clientAdvertisementService.saveRepo(clientAdvertisement);

            clientAdvertisementBid.setClientAdvertisement(clientAdvertisement);
            clientAdvertisementBid.setBidStatus(BidStatus.COMPLETED);

            clientAdvertisementBid = clientAdvertisementBidService.saveRepo(clientAdvertisementBid);

            clientTransportProcess.setAcceptedClientAdvertisementBid(clientAdvertisementBid);

            clientTransportProcessRepository.save(clientTransportProcess);


            TransportPayment transportPayment = new TransportPayment();
            transportPayment.setTransportProcess(clientTransportProcess);
            transportPayment.setPaymentStatus(PaymentStatus.PAYMENT_WAITING);


            double paymentToDriver = AdvertisementBidLogic.round(((clientTransportProcess.getPrice()) -
                    ((clientTransportProcess.getPrice()) *
                            (clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany().getCommissionRate() / 100D))), 2);

            transportPayment.setPrice(paymentToDriver);
            transportPaymentService.saveRepo(transportPayment);

            //TODO SONRADAN MESAJI KALDIR.
            return message;

        } else {

            clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.CLIENT_CARGO_PROBLEM);
            clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.CLIENT_CARGO_PROBLEM);
            clientTransportProcess.setClientExplanation(explanation);
            clientTransportProcessRepository.save(clientTransportProcess);
            return "MÜŞTERİ KARGO PARASININ AKTARILMASINI ONAYLAMADI. SORUN ÇIKMIŞTIR.";
        }
    }


    // Burada Teklifteki Ücret  Driver'ın Şirketinin Banka Hesabına Aktarılır.
    @Override
    public String paymentCompany(Long clientTransportProcessId) {

        ClientTransportProcess clientTransportProcess = findDataById(clientTransportProcessId);

        clientUnauthorizedTransactionCheck(clientTransportProcess);

        if (clientTransportProcess.getTransportProcessStatus() != TransportProcessStatus.COMPLETED) {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.clientTransportProcess.TransportProcessStatusException.cargo"));
        }

        Driver driver = clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder();
        Company company = clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany();

        return " Sürücü : " + driver.getFirstName() + " " + driver.getLastName() + ", " + clientTransportProcess.getTransportCode() +
                " numaralı kargoyu başarıyla teslim etmiştir. \n" +
                clientTransportProcess.getPrice() + "TL'lik ücret " + company.getCommercialTitle() + " şirketinin banka hesabına aktarılmıştır. \n" +
                "Lojister'ı Tercih Ettiğiniz İçin Teşekkürler.";

    }

    @Override
    public void startingTransport(Long id) throws MessagingException, UnsupportedEncodingException {
        ClientTransportProcess clientTransportProcess = findDataById(id);
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findByTransportProcessId(id);
        if (optionalConfirmationToken.isPresent()) {
            confirmationTokenService.delete(optionalConfirmationToken.get().getId());
        }
        ConfirmationToken confirmationToken = confirmationTokenService.save(ConfirmationToken.create(clientTransportProcess));
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.STARTING_TRANSPORT);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.STARTING_TRANSPORT);
        SendMailConfirmationTransportDto sendMailConfirmationTransportDto = new SendMailConfirmationTransportDto(confirmationToken.getToken(), confirmationToken.getClientTransportProcess().getTransportCode(), confirmationToken.getEmail());
        confirmationTokenMailSender.send(sendMailConfirmationTransportDto, ConfirmationTokenMailSender.Status.STARTING);
    }

    @Override
    public void endingTransport(Long id) throws MessagingException, UnsupportedEncodingException {
        ClientTransportProcess clientTransportProcess = findDataById(id);
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findByTransportProcessId(id);
        if (optionalConfirmationToken.isPresent()) {
            confirmationTokenService.delete(optionalConfirmationToken.get().getId());
        }
        ConfirmationToken confirmationToken = confirmationTokenService.save(ConfirmationToken.create(clientTransportProcess));
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.ENDING_TRANSPORT);
        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.ENDING_TRANSPORT);
        SendMailConfirmationTransportDto sendMailConfirmationTransportDto = new SendMailConfirmationTransportDto(confirmationToken.getToken(), confirmationToken.getClientTransportProcess().getTransportCode(), confirmationToken.getEmail());
        confirmationTokenMailSender.send(sendMailConfirmationTransportDto, ConfirmationTokenMailSender.Status.ENDING);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public ClientTransportProcess calculateTheFeeByInsuranceType(ClientTransportProcess clientTransportProcess) {

        if (clientTransportProcess.getInsuranceType() == InsuranceType.NARROW) {
            clientTransportProcess.calculatePriceForInsuranceByRatio(NARROW_INSURANCE_RATIO);
            clientTransportProcess.setInsuranceType(InsuranceType.NARROW);
        } else if (clientTransportProcess.getInsuranceType() == InsuranceType.COMPREHENSIVE) {
            clientTransportProcess.calculatePriceForInsuranceByRatio(COMPREHENSIVE_INSURANCE_RATIO);
            clientTransportProcess.setInsuranceType(InsuranceType.COMPREHENSIVE);
        }

        clientTransportProcessRepository.save(clientTransportProcess);
        return clientTransportProcess;
    }

    @Override
    public void changeStatusTrueAfterUploadingTheFile(ClientTransportProcess clientTransportProcess) {
        clientTransportProcessRepository.save(clientTransportProcess.insuredFileStatusChangeTrue());
    }

    @Override
    public List<ClientTransportProcessWithClientAdvertisementDto> findByDriverIdAndTransportProcessStatus(List<TransportProcessStatus> transportProcessStatusList) {
        Driver driver = securityContextUtil.getCurrentDriver();
        return clientTransportProcessWithClientAdvertisementDtoMapper.entityListToDtoList(clientTransportProcessRepository
                .findByDriverIdAndTransportProcessStatus(driver.getId()
                        , transportProcessStatusList));


    }


    @Override
    public Long countByAcceptedClientAdvertisementBid_DriverBidder_IdAndCreatedDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        Driver driver = securityContextUtil.getCurrentDriver();
        return clientTransportProcessRepository.countByAcceptedClientAdvertisementBid_DriverBidder_IdAndCreatedDateTimeBetween(driver.getId(), start, end);
    }

    @Override
    public void uploadDriverTransportProcessDocumentFile(FileUploadUtil.FileResult result, Long transportProcessId, SaveDocumentFileRequest saveDocumentFileRequest) {

        DocumentFile documentFile = new DocumentFile();
        User user = securityContextUtil.getCurrentDriver();
        ClientTransportProcess clientTransportProcess = findDataById(transportProcessId);

        clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().setAdvertisementProcessStatus(AdvertisementProcessStatus.UPLOADED_DOCUMENT);
        clientTransportProcess.setTransportProcessStatus(TransportProcessStatus.UPLOADED_DOCUMENT);
        try (TempFileUtil.TempFileResult tempFileResult = TempFileUtil.cache(result.getFilename(), result.getFileStream())) {
            Blob blob = BlobProxy.generateProxy(tempFileResult.getFileInputStream(), tempFileResult.getLength());
            documentFile.setData(blob);
            documentFile.setFileName(result.getFilename());
            documentFile.setDocumentType(saveDocumentFileRequest.getDocumentType());
            documentFile.setUser(user);
            documentFile.setClientTransportProcess(clientTransportProcess);
            documentService.save(documentFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DocumentResponse> getDocumentList(Long id) {
        List<DocumentFileWithoutDataDto> documentFileList=documentService.findTransportProcessId(id);
        List<DocumentResponse> documentResponseList= documentFileList.stream().map(documentFile -> {
            DocumentResponse documentResponse = new DocumentResponse();
            documentResponse.setId(documentFile.getId());
            documentResponse.setDocumentType(documentFile.getDocumentType());
            documentResponse.setUserId(documentFile.getUser().getId());
            documentResponse.setFileName(documentFile.getFileName());
            documentResponse.setContentType(documentFile.getContentType());
            documentResponse.setUrl(apiUrl+"/document/"+documentFile.getId()+"/url");
            return documentResponse;

        }).collect(Collectors.toList());

        return documentResponseList;
    }

}
