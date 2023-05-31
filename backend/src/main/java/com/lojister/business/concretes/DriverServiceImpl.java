package com.lojister.business.concretes;

import com.lojister.business.abstracts.ClientAdvertisementBidService;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.controller.driver.DriverDashboardResponse;
import com.lojister.controller.driver.UpdateDriverNotificationSettingRequest;
import com.lojister.core.i18n.Translator;
import com.lojister.mapper.DriverMinDtoMapper;
import com.lojister.mapper.DriverNotificationSettingDtoMapper;
import com.lojister.model.dto.*;
import com.lojister.model.dto.driver.*;
import com.lojister.model.enums.*;
import com.lojister.core.exception.DriverStatusException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.core.exception.WrongPasswordMatchException;
import com.lojister.mapper.DriverMapper;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.driver.DriverDocumentFileRepository;
import com.lojister.repository.driver.DriverRepository;
import com.lojister.core.security.JwtTokenUtil;
import com.lojister.business.abstracts.DriverService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.core.validator.DuplicateEmailValidator;
import com.lojister.core.validator.DuplicatePhoneValidator;
import com.lojister.core.validator.UpdateOperationDuplicateEmailValidator;
import com.lojister.core.validator.UpdateOperationDuplicatePhoneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverDocumentFileRepository driverDocumentFileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DriverMapper driverMapper;
    private final JwtTokenUtil jwtTokenUtil;
    @Lazy
    private final SecurityContextUtil securityContextUtil;
    private final PhoneFormatter phoneFormatter;
    private final DuplicatePhoneValidator duplicatePhoneValidator;
    private final DuplicateEmailValidator duplicateEmailValidator;
    private final UpdateOperationDuplicatePhoneValidator updateOperationDuplicatePhoneValidator;
    private final UpdateOperationDuplicateEmailValidator updateOperationDuplicateEmailValidator;
    private final ClientTransportProcessService clientTransportProcessService;
    private final DriverMinDtoMapper driverMinDtoMapper;
    private final ClientAdvertisementBidService clientAdvertisementBidService;
    private final DriverNotificationSettingDtoMapper driverNotificationSettingDtoMapper;

    @Override
    public DriverDto save(DriverEmployeeSaveDto driverDto) {

        Driver driverBoss = securityContextUtil.getCurrentDriver();

        duplicatePhoneValidator.validate(driverDto.getPhone());
        duplicateEmailValidator.validate(driverDto.getEmail());

        Driver driver = new Driver();

        driver.setFirstName(driverDto.getFirstName());
        driver.setLastName(driverDto.getLastName());
        driver.setEmail(driverDto.getEmail());
        driver.setPassword(bCryptPasswordEncoder.encode(driverDto.getPassword()));
        driver.setPhone(phoneFormatter.format(driverDto.getPhone()));
        driver.setDriverTitle(DriverTitle.EMPLOYEE);
        driver.setCitizenId(driverDto.getCitizenId());
        driver.setBoss(driverBoss);
        driver.setCompany(driverBoss.getCompany());
        driver.setRole(Role.ROLE_DRIVER_EMPLOYEE);
        driver.setStatus(DriverStatus.REGISTERED);
        driver.setPhoneConfirmed(true);

        driver = driverRepository.save(driver);

        return driverMapper.entityToDto(driver);

    }


    //Role Driver ise Boss'u yoktur.
    @Override
    public DriverUpdateDto updateDriverAndGetNewToken(Long id, DriverUpdateRequestDto driverDto) {

        Driver driver = findDataById(id);

        Driver currentDriver = securityContextUtil.getCurrentDriver();

        if (driver.getRole() == Role.ROLE_DRIVER) {

            unauthorizedTransactionCheck(driver, currentDriver);
            updateDriverEmailCheck(driver, driverDto);
            updateDriverPhoneCheck(driver, driverDto);

            driver = updateDriverWithTokenSaveDriver(driver, driverDto);

            return createDriverUpdateDto(driver);


        } else if (driver.getRole() == Role.ROLE_DRIVER_EMPLOYEE) {

            //Sadece patron düzenleyebiliyor. Çalışan kendisini düzenleyemez.
            unauthorizedTransactionCheck(driver.getBoss(), currentDriver);
            updateDriverEmailCheck(driver, driverDto);
            updateDriverPhoneCheck(driver, driverDto);

            driver = updateDriverWithTokenSaveDriver(driver, driverDto);

            return createDriverUpdateDto(driver);

        } else {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.driver.UnauthorizedTransactionException"));
        }
    }


    @Override
    public DriverDto getById(Long id) {

        Driver driver = findDataById(id);

        return driverMapper.entityToDto(driver);
    }

    @Override
    public void deleteById(Long id) {

        Driver driver = findDataById(id);

        driverRepository.deleteById(driver.getId());
    }

    @Override
    //TODO BURAYA BAK.
    public List<DriverDto> getAll() {

        return driverMapper.entityListToDtoList(driverRepository.findAll());
    }


    @Override
    public Page<MyDriversPageDto> getMyDrivers(Pageable pageable) {
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().isEmpty() ? Sort.by("createdDateTime").descending() : pageable.getSort());
        Page<MyDriversPageDto> myDriversPageDto = driverRepository.getMyDriversByCompanyId(securityContextUtil.getCurrentDriver().getCompany().getId(), customPageable);

        return myDriversPageDto;
    }

    @Override
    public List<DriverMinDto> getMyDrivers() {
        return driverMinDtoMapper.entityListToDtoList(driverRepository.getMyDriversByCompanyId(securityContextUtil.getCurrentDriver().getCompany().getId()));
    }


    @Override
    public List<DriverAndDocumentListDto> getReviewStatus() {
        List<Driver> driverList = driverRepository.findDistinctByStatusIn(Arrays.asList(DriverStatus.REVIEW));
        return getDriverAndDocumentListDtoList(driverList);

    }


    @Override
    public List<DriverAndDocumentListDto> getRevisionStatus() {

        List<Driver> driverList = driverRepository.findDistinctByStatus(DriverStatus.REVISION);
        return getDriverAndDocumentListDtoList(driverList);
    }

    @Override
    public void updateDriverStatus(Boolean value, String statusDescription, Long driverId) {

        Driver driver = findDataById(driverId);

        if (driver.getStatus() == DriverStatus.REVIEW || driver.getStatus() == DriverStatus.REVIEW_SENT) {

            if (value) {

                driver.setStatusDescription(statusDescription);
                driver.setStatus(DriverStatus.ACCEPTED);

            } else {

             /*   List<DriverDocumentFile> driverDocumentFileList = driverDocumentFileRepository.findByDriver_Id(driverId);

                if (!driverDocumentFileList.isEmpty()) {

                    for (DriverDocumentFile driverDocumentFile1 : driverDocumentFileList) {

                        driverDocumentFileRepository.deleteById(driverDocumentFile1.getId());
                    }
                }*/

                driver.setStatus(DriverStatus.REVIEW_SENT);
                driver.setStatusDescription(statusDescription);
            }
        } else if (driver.getStatus() == DriverStatus.REVISION) {

            if (value) {

                driver.setStatusDescription(statusDescription);
                driver.setStatus(DriverStatus.ACCEPTED);

            } else {
             /*   List<DriverDocumentFile> driverDocumentFileList = driverDocumentFileRepository.findByDriver_Id(driverId);

                if (!driverDocumentFileList.isEmpty()) {

                    for (DriverDocumentFile driverDocumentFile1 : driverDocumentFileList) {

                        driverDocumentFileRepository.deleteById(driverDocumentFile1.getId());
                    }
                }*/
                driver.setStatusDescription(statusDescription);
                driver.setStatus(DriverStatus.REVIEW_SENT);

            }
        } else {
            throw new DriverStatusException(Translator.toLocale("lojister.driver.DriverStatusException"));
        }

        driverRepository.save(driver);


    }


    @Override
    public Boolean changePassword(String oldPassword, String nwPassword) {

        Driver driver = securityContextUtil.getCurrentDriver();

        boolean isMatches = bCryptPasswordEncoder.matches(oldPassword, driver.getPassword());

        if (isMatches) {

            driver.setPassword(bCryptPasswordEncoder.encode(nwPassword));
            driverRepository.save(driver);
            return true;

        } else {
            throw new WrongPasswordMatchException(Translator.toLocale("lojister.driver.WrongPasswordMatchException"));
        }
    }


    @Override
    public DriverUpdateDto createDriverUpdateDto(Driver driver) {

        DriverUpdateDto driverUpdateDto = new DriverUpdateDto();
        driverUpdateDto.setId(driver.getId());
        driverUpdateDto.setFirstName(driver.getFirstName());
        driverUpdateDto.setLastName(driver.getLastName());
        driverUpdateDto.setCitizenId(driver.getCitizenId());
        driverUpdateDto.setEmail(driver.getEmail());
        driverUpdateDto.setPhone(driver.getPhone());
        //TODO LOGOUT YAP
        // driverUpdateDto.setToken(jwtTokenUtil.generateToken(driver, false));

        return driverUpdateDto;
    }

    @Override
    public Driver findDataById(Long id) {

        Optional<Driver> driver = driverRepository.findById(id);

        if (driver.isPresent()) {

            return driver.get();

        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.driver.EntityNotFoundException.driver"));
        }
    }

    @Override
    public Driver updateDriverWithTokenSaveDriver(Driver driver, DriverUpdateRequestDto driverDto) {

        driver.setFirstName(driverDto.getFirstName());
        driver.setLastName(driverDto.getLastName());
        driver.setPhone(phoneFormatter.format(driverDto.getPhone()));
        driver.setEmail(driverDto.getEmail());
        driver.setCitizenId(driverDto.getCitizenId());
        driver = driverRepository.save(driver);

        return driver;
    }

    @Override
    public Driver saveRepo(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void unauthorizedTransactionCheck(Driver driver, Driver currentDriver) {

        if (!(currentDriver.getId().equals(driver.getId()))) {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.driver.UnauthorizedTransactionException"));
        }

    }

    @Override
    public void updateDriverEmailCheck(Driver driver, DriverUpdateRequestDto driverDto) {

        UpdateUserEmailCheckDto updateUserEmailCheckDto = new UpdateUserEmailCheckDto(driver, driverDto.getEmail());

        updateOperationDuplicateEmailValidator.validate(updateUserEmailCheckDto);

    }

    @Override
    public void updateDriverPhoneCheck(Driver driver, DriverUpdateRequestDto driverDto) {

        UpdateUserPhoneCheckDto updateUserPhoneCheckDto = new UpdateUserPhoneCheckDto(driver, driverDto.getPhone());

        updateOperationDuplicatePhoneValidator.validate(updateUserPhoneCheckDto);
    }

    @Override
    public List<DriverAndDocumentListDto> getDriverAndDocumentListDtoList(List<Driver> driverList) {
        List<DriverAndDocumentListDto> driverAndDocumentListDtoList = new ArrayList<>();


        for (Driver driver : driverList) {
            DriverAndDocumentListDto driverAndDocumentListDto = new DriverAndDocumentListDto();
            driverAndDocumentListDto.setDriverDto(driverMapper.entityToDto(driver));
            List<DriverDocumentMinimalDto> documentMinimalDtoList = driverDocumentFileRepository.findMinimalDriverDocumentByDriverId(driver.getId());
            driverAndDocumentListDto.setDocumentMinimalDtoList(documentMinimalDtoList);

            driverAndDocumentListDtoList.add(driverAndDocumentListDto);
        }

        return driverAndDocumentListDtoList;
    }

    @Override
    public Driver findByCompanyIdAndDriverTitle(Long companyId, DriverTitle driverTitle) {

        Optional<Driver> optionalDriver = driverRepository.findByCompany_IdAndDriverTitle(companyId, driverTitle);

        if (optionalDriver.isPresent()) {

            return optionalDriver.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.driver.EntityNotFoundException.driverboss"));
        }
    }

    @Override
    public List<String> getAllFirebaseTokenFromDrivers() {

        List<Token> allToken = driverRepository.findAllByDriverTitle(DriverTitle.BOSS);

        return Optional.ofNullable(allToken)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .map(Token::getFirebaseToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public DriverDashboardResponse getDashboardInformation() {
        DriverDashboardResponse driverDashboardResponse = new DriverDashboardResponse();

        driverDashboardResponse.setActiveAdvertisementBidCount(clientAdvertisementBidService
                .countByDriverIdAndInBidStatus(Arrays.asList(BidStatus.WAITING, BidStatus.TRANSPORT, BidStatus.APPROVED)));

        driverDashboardResponse.setTransportAdvertisementBidCount(clientAdvertisementBidService
                .countByDriverIdAndInBidStatus(Arrays.asList(BidStatus.TRANSPORT)));

        driverDashboardResponse.setFinishedTransportCount(clientAdvertisementBidService
                .countByDriverIdAndInBidStatus(Arrays.asList(BidStatus.COMPLETED)));

        driverDashboardResponse.setLastMonthCount(clientTransportProcessService
                .countByAcceptedClientAdvertisementBid_DriverBidder_IdAndCreatedDateTimeBetween(LocalDateTime.now().minusMonths(1), LocalDateTime.now()));

        driverDashboardResponse.setClientAdvertisementBidList(clientAdvertisementBidService.getDriverIdAndAdvertisementProcessStatusIn(securityContextUtil.getCurrentDriver().getId(),
                Arrays.asList(AdvertisementProcessStatus.ASSIGNED_VEHICLE,
                        AdvertisementProcessStatus.UPLOADED_DOCUMENT,
                        AdvertisementProcessStatus.CARGO_DELIVERED,
                        AdvertisementProcessStatus.CARGO_ON_THE_WAY,
                        AdvertisementProcessStatus.WAYBILL_DENIED,
                        AdvertisementProcessStatus.PAYMENT_SUCCESSFUL
                        )
                )
        );
        return driverDashboardResponse;
    }

    @Override
    public DriverNotificationSettingDto updateNotificationSetting(UpdateDriverNotificationSettingRequest updateDriverNotificationSettingRequest) {
        Driver driver = securityContextUtil.getCurrentDriver();
        driver = driverRepository.save(driver.updateNotificationSetting(updateDriverNotificationSettingRequest));
        return driverNotificationSettingDtoMapper.entityToDto(driver.getNotificationSetting());
    }

    @Override
    public DriverNotificationSettingDto getNotificationSetting() {
        Driver driver = securityContextUtil.getCurrentDriver();
        return driverNotificationSettingDtoMapper.entityToDto(driver.getNotificationSetting());
    }

    @Override
    public List<Driver> findByDriverTitleIn(List<DriverTitle> driverTitleList) {
        return driverRepository.findByDriverTitleIn(driverTitleList);
    }


}








