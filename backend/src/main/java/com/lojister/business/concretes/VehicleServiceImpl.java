package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.business.abstracts.*;
import com.lojister.core.exception.*;
import com.lojister.mapper.VehicleMinDtoMapper;
import com.lojister.model.entity.*;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.enums.TransportProcessType;
import com.lojister.model.enums.VehicleStatus;
import com.lojister.mapper.CompanyMapper;
import com.lojister.mapper.DriverMapper;
import com.lojister.mapper.VehicleMapper;
import com.lojister.model.dto.*;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.adminpanel.VehicleType;
import com.lojister.repository.vehicle.VehicleRepository;
import com.lojister.repository.vehicle.VehicleTypeRepository;
import com.lojister.core.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleDocumentFileService vehicleDocumentFileService;
    private final DriverService driverService;
    private final VehicleMapper vehicleMapper;
    private final CompanyMapper companyMapper;
    private final DriverMapper driverMapper;
    private final SecurityContextUtil securityContextUtil;
    @Lazy
    private final ClientTransportProcessService clientTransportProcessService;
    private final TransportProcessService transportProcessService;
    private final VehicleMinDtoMapper vehicleMinDtoMapper;


    //TODO BURAYA SONRADAN BAK İÇİME SİNMEDİ.
    @Override
    public VehicleDto save(VehicleDto vehicleDto) {

        Vehicle vehicle = new Vehicle();

        if (vehicleDto.getDriver() != null) {

            Driver driver = driverService.findDataById(vehicleDto.getDriver().getId());

            vehicleAssignedToDriverCheck(driver);

            vehicle.setDriver(driver);

        }

        //TODO BURAYA BİDA BAK. VEHICLETYPE OLMAYABİLİRSE NULL YER MİSİN KONTROL ET.
        Optional<VehicleType> vehicleType = vehicleTypeRepository.findById(vehicleDto.getVehicleType().getId());

        if (vehicleType.isPresent()) {

            vehicle.setVehicleType(vehicleType.get());
        }

        if (vehicleType.get().getTypeName().equalsIgnoreCase("TIR")) {
            vehicle.setTrailerPlate(vehicleDto.getTrailerPlate());
            //TODO EĞER TIRSA DORSE PLAKASI BOŞ GEÇİLEBİLİR Mİ ONU SOR.
        }


        Long vehicleCount = vehicleRepository.countByVehicleModelAndBrand(vehicleDto.getVehicleModel(), vehicleDto.getBrand());


        vehicle.setVehicleCount(++vehicleCount);
        vehicle.setCompany(securityContextUtil.getCurrentDriver().getCompany());
        vehicle.setBrand(vehicleDto.getBrand());
        vehicle.setLicencePlate(vehicleDto.getLicencePlate());
        vehicle.setMaxCapacity(vehicleDto.getMaxCapacity());
        vehicle.setVehicleModel(vehicleDto.getVehicleModel());
        vehicle.setVehicleName(vehicleDto.getVehicleName());
        vehicle.setVehicleStatus(VehicleStatus.REGISTERED);
        vehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.entityToDto(vehicle);
    }

    @Override
    public VehicleDto update(Long id, VehicleDto vehicleDto) {

        Vehicle vehicleData = findDataById(id);
        vehicleData.setMaxCapacity(vehicleDto.getMaxCapacity());
        vehicleData.setLicencePlate(vehicleDto.getLicencePlate());
        vehicleData.setCompany(companyMapper.dtoToEntity(vehicleDto.getCompany()));
        vehicleData.setDriver(driverMapper.dtoToEntity(vehicleDto.getDriver()));
        vehicleData = vehicleRepository.save(vehicleData);

        return vehicleMapper.entityToDto(vehicleData);
    }

    @Override
    public VehicleDto getById(Long id) {

        Vehicle vehicle = findDataById(id);

        return vehicleMapper.entityToDto(vehicle);

    }

    @Override
    public void deleteById(Long id) {

        findDataById(id);
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleDto> getAll() {

        return vehicleMapper.entityListToDtoList(vehicleRepository.findAll());
    }


    @Override
    public List<VehicleAndDocumentListDto> getReviewStatus() {

        List<Vehicle> vehicleList = vehicleRepository.findByVehicleStatus(VehicleStatus.REVIEW);
        return getVehicleAndDocumentListDtoList(vehicleList);
    }


    @Override
    public List<VehicleAndDocumentListDto> getRevisionStatus() {

        List<Vehicle> vehicleList = vehicleRepository.findByVehicleStatus(VehicleStatus.REVISION);
        return getVehicleAndDocumentListDtoList(vehicleList);
    }

    @Override
    public List<VehicleAndDocumentListDto> getVehicleAndDocumentListDtoList(List<Vehicle> vehicleList) {
        List<VehicleAndDocumentListDto> vehicleAndDocumentListDtoList = new ArrayList<>();
        VehicleAndDocumentListDto vehicleAndDocumentListDto = new VehicleAndDocumentListDto();

        for (Vehicle vehicle : vehicleList) {

            vehicleAndDocumentListDto.setVehicleDto(vehicleMapper.entityToDto(vehicle));
            List<VehicleDocumentMinimalDto> documentMinimalDtoList = vehicleDocumentFileService.findMinimalVehicleDocumentByVehicleIdRepo(vehicle.getId());
            vehicleAndDocumentListDto.setDocumentMinimalDtoList(documentMinimalDtoList);

            vehicleAndDocumentListDtoList.add(vehicleAndDocumentListDto);
        }

        return vehicleAndDocumentListDtoList;
    }

    @Override
    public void vehicleAssignedToDriverCheck(Driver driver) {

        Optional<Vehicle> vehicleControl = vehicleRepository.findByDriver_Id(driver.getId());

        if (vehicleControl.isPresent()) {
            throw new DriverAssignedAnotherVehicleException(Translator.toLocale("lojister.vehicle.DriverAssignedAnotherVehicleException"));
        }
    }

    @Override
    public void vehicleAcceptedCheck(Vehicle vehicle) {

        if(Optional.ofNullable(vehicle).isPresent()){
            if(vehicle.getVehicleStatus()!=VehicleStatus.ACCEPTED){
              throw  new DriverIsAcceptedException(Translator.toLocale("lojister.vehicle.DriverIsAcceptedException"));
            }
        }
    }


    @Override
    public Page<MyVehiclesPageDto> getMyVehicles(Pageable pageable) {
        org.springframework.data.domain.Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().isEmpty() ? Sort.by("createdDateTime").descending() : pageable.getSort());
        Page<MyVehiclesPageDto> myVehiclesPageDtos = vehicleRepository.getMyVehiclesByCompanyId(securityContextUtil.getCurrentDriver().getCompany().getId(), pageable);
        return myVehiclesPageDtos;

    }

    @Override
    public List<VehicleMinDto> getMyVehicles() {
        return vehicleMinDtoMapper.entityListToDtoList(vehicleRepository.getMyVehiclesByCompanyId(securityContextUtil.getCurrentDriver().getCompany().getId()));
    }

    @Override
    public VehicleDto changeDriver(Long vehicleId, Long driverId,Long transportProcessId) {

        Vehicle vehicle = findDataById(vehicleId);

        Driver driverBoss = driverService.findByCompanyIdAndDriverTitle(vehicle.getCompany().getId(), DriverTitle.BOSS);

        if (driverBoss.getId() != securityContextUtil.getCurrentDriver().getId()) {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.vehicle.UnauthorizedTransactionException"));
        }

        Driver driver = driverService.findDataById(driverId);

        vehicleAssignedToDriverCheck(driver);

        //TODO SÜRÜCÜ İLLA Kİ YOLDA OLMAYABİLİR.
        //TODO STATULERE DİKKAT ET ONLARA BAK.
        ClientTransportProcess clientTransportProcess= clientTransportProcessService.findDataById(transportProcessId);
        SummaryDriverData summaryDriverData = new SummaryDriverData();
        summaryDriverData.setCitizenId(driver.getCitizenId());
        summaryDriverData.setCompanyName(driver.getCompany().getCommercialTitle());
        summaryDriverData.setFirstName(driver.getFirstName());
        summaryDriverData.setLastName(driver.getLastName());
        summaryDriverData.setMail(driver.getEmail());
        summaryDriverData.setPhone(driver.getPhone());
        clientTransportProcess.setSummaryDriverData(summaryDriverData);
        clientTransportProcessService.saveRepo(clientTransportProcess);
        vehicle.setDriver(driver);

        return vehicleMapper.entityToDto(vehicleRepository.save(vehicle));

    }

    @Override
    public Boolean unAssignDriverFromVehicle(Long vehicleId) {

        Driver currentDriverBoss = securityContextUtil.getCurrentBossDriver();

        Vehicle vehicle = findDataById(vehicleId);

        unAssignDriverFromVehiclePermissionCheck(vehicle, currentDriverBoss);
        vehicleOnTheRoadCheck(vehicle);


        vehicle.setDriver(null);
        vehicleRepository.save(vehicle);
        return true;
    }


    @Override
    public void unAssignDriverFromVehiclePermissionCheck(Vehicle vehicle, Driver boss) {

        if (vehicle.getCompany().getId() != boss.getCompany().getId()) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.vehicle.UnauthorizedTransactionException"));
        }
    }

    @Override
    public void vehicleOnTheRoadCheck(Vehicle vehicle) {

        List<ClientTransportProcess> clientTransportProcessList = clientTransportProcessService.findByVehicleIdAndTransportProcessStatusRepo(vehicle.getId(), TransportProcessStatus.CARGO_ON_THE_WAY);

        if (!clientTransportProcessList.isEmpty()) {
            throw new VehicleOnTheRoadException(Translator.toLocale("lojister.vehicle.VehicleOnTheRoadException"));
        }

    }


    @OnlyAdmin
    @Override
    public void updateVehicleStatus(Boolean value, String statusDescription, Long vehicleId) {

        Vehicle vehicle = findDataById(vehicleId);

        if (vehicle.getVehicleStatus() == VehicleStatus.REVIEW) {

            if (value) {

                vehicle.setStatusDescription(statusDescription);
                vehicle.setVehicleStatus(VehicleStatus.ACCEPTED);

            } else {

                List<VehicleDocumentFile> vehicleDocumentFileList = vehicleDocumentFileService.findByVehicleIdRepo(vehicleId);

                if (!vehicleDocumentFileList.isEmpty()) {

                    for (VehicleDocumentFile vehicleDocumentFile1 : vehicleDocumentFileList) {

                        vehicleDocumentFileService.deleteById(vehicleDocumentFile1.getId());
                    }
                }

                vehicle.setVehicleStatus(VehicleStatus.REVIEW_SENT);
                vehicle.setStatusDescription(statusDescription);
            }
        } else if (vehicle.getVehicleStatus() == VehicleStatus.REVISION) {

            if (value) {

                vehicle.setStatusDescription(statusDescription);
                vehicle.setVehicleStatus(VehicleStatus.ACCEPTED);

            } else {
                vehicle.setStatusDescription(statusDescription);
                vehicle.setVehicleStatus(VehicleStatus.DENIED);
            }
        } else {
            throw new VehicleStatusException(Translator.toLocale("lojister.vehicle.VehicleStatusException"));
        }

        vehicleRepository.save(vehicle);

    }

    @Override
    public void updateLastLocation(PositionDto positionDto) {

        Driver currentDriver = securityContextUtil.getCurrentDriver();

        Optional<Vehicle> vehicle = vehicleRepository.findByDriver_Id(currentDriver.getId());

        if (vehicle.isPresent()) {

            Position position = new Position();
            position.setLatitude(positionDto.getLatitude());
            position.setLongitude(positionDto.getLongitude());
            position.setLogDate(LocalDate.now());
            position.setLogTime(LocalTime.now());
            vehicle.get().setLastPosition(position);
            vehicleRepository.save(vehicle.get());

        } else {
            throw new AssignedVehicleToDriverException(Translator.toLocale("lojister.vehicle.AssignedVehicleToDriverException"));
        }

    }

    @Override
    public LastPositionResponseDto getDriverLastPositionByTransportProcessId(Long transportProcessId) {

        TransportProcess transportProcess = transportProcessService.findDataById(transportProcessId);

        Long currentUserId = securityContextUtil.getCurrentUserId();


        if (transportProcess.getTransportProcessStatus() != TransportProcessStatus.CARGO_ON_THE_WAY) {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.vehicle.TransportProcessStatusException"));
        }

        LastPositionResponseDto lastPositionResponseDto = new LastPositionResponseDto();

        if (transportProcess.getTransportProcessType() == TransportProcessType.CLIENT_TRANSPORT_PROCESS) {

            ClientTransportProcess clientTransportProcess = (ClientTransportProcess) transportProcess;

       /*     if (!((clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getId() == currentUserId) ||
                    (clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getId() == currentUserId))) {

                throw new UnauthorizedTransactionException("Yetkisiz işlem", "Unauthorized process.");

            }*/

            //LAST POSİTİON YOKSA BURADA PATLIYOR VE NULL POİNTER VERİYOR.
            if (clientTransportProcess.getVehicle().getLastPosition() == null) {

                throw new LastPositionNotExistException(Translator.toLocale("lojister.vehicle.LastPositionNotExistException"));

            }

            lastPositionResponseDto.setLatitude(clientTransportProcess.getVehicle().getLastPosition().getLatitude());
            lastPositionResponseDto.setLongitude(clientTransportProcess.getVehicle().getLastPosition().getLongitude());
            lastPositionResponseDto.setLogDate(clientTransportProcess.getVehicle().getLastPosition().getLogDate());
            lastPositionResponseDto.setLogTime(clientTransportProcess.getVehicle().getLastPosition().getLogTime());

            lastPositionResponseDto.setTransportProcessType(clientTransportProcess.getTransportProcessType());
            lastPositionResponseDto.setTransportCode(clientTransportProcess.getTransportCode());
            lastPositionResponseDto.setTransportId(clientTransportProcess.getId());
            lastPositionResponseDto.setLicencePlate(clientTransportProcess.getVehicle().getLicencePlate());
            return lastPositionResponseDto;

        } else if (transportProcess.getTransportProcessType() == TransportProcessType.DRIVER_TRANSPORT_PROCESS) {

            //SONRADAN YAPILACAK.

            return null;

        } else {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.vehicle.TransportProcessStatusException.statusNotFound"));
        }

    }

    @Override
    public Vehicle findDataById(Long vehicleId) {

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle.isPresent()) {

            return vehicle.get();

        } else {

            throw new EntityNotFoundException(Translator.toLocale("lojister.vehicle.EntityNotFoundException"));
        }
    }

    @Override
    public Vehicle saveRepo(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }


    @Override
    public void driverNullCheck(Vehicle vehicle) {

        if (vehicle.getDriver() == null) {
            throw new AssignedDriverToVehicleException(Translator.toLocale("lojister.vehicle.AssignedDriverToVehicleException"));
        }
    }


}
