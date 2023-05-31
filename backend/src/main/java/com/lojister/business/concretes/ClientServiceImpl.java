package com.lojister.business.concretes;

import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.business.abstracts.SavedAddressService;
import com.lojister.controller.client.ClientDashboardResponse;
import com.lojister.controller.client.SaveClientEmployeeRequest;
import com.lojister.controller.client.UpdateClientAccountSettingRequest;
import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.core.i18n.Translator;
import com.lojister.core.validator.DuplicateEmailValidator;
import com.lojister.core.validator.DuplicatePhoneValidator;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.mapper.*;
import com.lojister.model.dto.SavedAddressDto;
import com.lojister.model.dto.UpdateUserEmailCheckDto;
import com.lojister.model.dto.UpdateUserPhoneCheckDto;
import com.lojister.model.dto.client.*;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalDto;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.SavedAddress;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.Role;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.core.exception.WrongPasswordMatchException;
import com.lojister.model.entity.client.Client;
import com.lojister.repository.client.ClientRepository;
import com.lojister.core.security.JwtTokenUtil;
import com.lojister.business.abstracts.ClientService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.UpdateOperationDuplicateEmailValidator;
import com.lojister.core.validator.UpdateOperationDuplicatePhoneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    @Lazy
    private final SecurityContextUtil securityContextUtil;
    private final ClientAdvertisementService clientAdvertisementService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final PhoneFormatter phoneFormatter;
    private final DuplicatePhoneValidator duplicatePhoneValidator;
    private final DuplicateEmailValidator duplicateEmailValidator;
    private final UpdateOperationDuplicateEmailValidator updateOperationDuplicateEmailValidator;
    private final UpdateOperationDuplicatePhoneValidator updateOperationDuplicatePhoneValidator;
    private final ClientAdvertisementMinimalDtoMapper clientAdvertisementMinimalDtoMapper;
    private final ClientNotificationSettingDtoMapper clientNotificationSettingDtoMapper;
    private  final SavedAddressService savedAddressService;
    private final ClientAccountSettingDtoMapper clientAccountSettingDtoMapper;
    private final SavedAddressMapper savedAddressMapper;

    //TODO SONRADAN BIDAHA BAK
    @Override
    public ClientUpdateResponseDto update(Long id, ClientUpdateRequestDto clientDto) {

        Client client = findDataById(id);

        Client currentClient = securityContextUtil.getCurrentClient();

        roleIsClientCheck(client);

        unauthorizedTransactionCheck(client, currentClient);
        updateClientPhoneCheck(client, clientDto);
        updateClientEmailCheck(client, clientDto);


        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setPhone(clientDto.getPhone());
        client.setEmail(clientDto.getEmail());
        clientRepository.save(client);

        //todo
        ClientUpdateResponseDto clientUpdateResponseDto = new ClientUpdateResponseDto();
        clientUpdateResponseDto.setId(client.getId());
        clientUpdateResponseDto.setFirstName(client.getFirstName());
        clientUpdateResponseDto.setLastName(client.getLastName());
        clientUpdateResponseDto.setEmail(client.getEmail());
        clientUpdateResponseDto.setPhone(client.getPhone());
        clientUpdateResponseDto.setToken(jwtTokenUtil.generateToken(client, false));

        return clientUpdateResponseDto;

    }


    @Override
    public ClientDto getById(Long id) {

        Client client = findDataById(id);

        return clientMapper.entityToDto(client);
    }

    @Override
    public void deleteById(Long id) {
        //ilan silinmesine sonra bakılacak. İlan gizlenebilecek.
    }


    @Override
    public List<ClientDto> getAll() {

        List<Client> clientList = clientRepository.findAll();

        return clientMapper.entityListToDtoList(clientList);
    }


    @Override
    public Boolean changePassword(String oldPassword, String nwPassword) {

        Client client = securityContextUtil.getCurrentClient();

        boolean isMatches = bCryptPasswordEncoder.matches(oldPassword, client.getPassword());

        if (isMatches) {

            client.setPassword(bCryptPasswordEncoder.encode(nwPassword));
            clientRepository.save(client);
            return true;

        } else {
            throw new WrongPasswordMatchException(Translator.toLocale("lojister.client.WrongPasswordMatchException"));
        }
    }

    @Override
    public Client saveRepo(Client client) {
        return clientRepository.save(client);
    }


    @Override
    public Client findDataById(Long id) {

        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.client.EntityNotFoundException"));
        }
    }

    @Override
    public void roleIsClientCheck(Client client) {

        if (client.getRole() != Role.ROLE_CLIENT) {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.client.UnauthorizedTransactionException.customer"));
        }
    }

    @Override
    public void unauthorizedTransactionCheck(Client client, Client currentClient) {

        if (!(currentClient.getId().equals(client.getId()))) {

            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.client.UnauthorizedTransactionException.other"));
        }
    }


    @Override
    public void updateClientEmailCheck(Client client, ClientUpdateRequestDto clientDto) {

        UpdateUserEmailCheckDto updateUserEmailCheckDto = new UpdateUserEmailCheckDto(client, clientDto.getEmail());

        updateOperationDuplicateEmailValidator.validate(updateUserEmailCheckDto);
    }

    @Override
    public void updateClientPhoneCheck(Client client, ClientUpdateRequestDto clientDto) {

        UpdateUserPhoneCheckDto updateUserPhoneCheckDto = new UpdateUserPhoneCheckDto(client, clientDto.getPhone());

        updateOperationDuplicatePhoneValidator.validate(updateUserPhoneCheckDto);
    }

    @Override
    public ClientDashboardResponse getDashboardInformation() {
        Long id = securityContextUtil.getCurrentClient().getId();
        ClientDashboardResponse clientDashboardResponse = new ClientDashboardResponse();
        Long activeAdvertisementCount = clientAdvertisementService.countByAdvertisementStatus(AdvertisementStatus.ACTIVE, id);
        Long transportAdvertisementCount = clientAdvertisementService.countByAdvertisementStatus(AdvertisementStatus.TRANSPORT, id);
        Long finishedAdvertisementCount = clientAdvertisementService.countByAdvertisementStatus(AdvertisementStatus.FINISHED, id);
        Long lastMonthCount = clientAdvertisementService.countByClientIdAndCreatedDateTimeBetween(id, LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        clientDashboardResponse.setFinishedAdvertisementCount(finishedAdvertisementCount);
        clientDashboardResponse.setActiveAdvertisementCount(activeAdvertisementCount);
        clientDashboardResponse.setTransportAdvertisementCount(transportAdvertisementCount);
        clientDashboardResponse.setLastMonthCount(lastMonthCount);
        List<ClientAdvertisementMinimalDto> clientAdvertisementMinimalDtoList = clientAdvertisementMinimalDtoMapper
                .entityListToDtoList(clientAdvertisementService.getByProcess(id));
        clientDashboardResponse.setClientAdvertisements(clientAdvertisementMinimalDtoList);
        return clientDashboardResponse;
    }

    @Override
    public ClientNotificationSettingDto updateNotificationSetting(UpdateClientNotificationSettingRequest updateClientNotificationSettingRequest) {
        Client client = securityContextUtil.getCurrentClient();
        client=clientRepository.save(client.updateNotificationSetting(updateClientNotificationSettingRequest));
        return clientNotificationSettingDtoMapper.entityToDto(client.getNotificationSetting());
    }

    @Override
    public ClientNotificationSettingDto getNotificationSetting() {
        Client client = securityContextUtil.getCurrentClient();
        return clientNotificationSettingDtoMapper.entityToDto(client.getNotificationSetting());
    }

    @Override
    public ClientAccountSettingDto updateAccountSetting(UpdateClientAccountSettingRequest updateClientAccountSettingRequest) {
        Client client = securityContextUtil.getCurrentClient();
        client=clientRepository.save(client.updateAccountSetting(updateClientAccountSettingRequest));
        return clientAccountSettingDtoMapper.entityToDto(client.getAccountSetting());
    }

    @Override
    public ClientAccountSettingDto getAccountSetting() {
        Client client = securityContextUtil.getCurrentClient();
        return clientAccountSettingDtoMapper.entityToDto(client.getAccountSetting());
    }

    @Override
    public ClientDto saveEmployee(SaveClientEmployeeRequest saveClientEmployeeRequest) {
        Client client= securityContextUtil.getCurrentClient();
        duplicatePhoneValidator.validate(saveClientEmployeeRequest.getPhone());
        duplicateEmailValidator.validate(saveClientEmployeeRequest.getEmail());
        return clientMapper.entityToDto(clientRepository.save(Client.saveEmployee(client,saveClientEmployeeRequest,bCryptPasswordEncoder.encode(saveClientEmployeeRequest.getPassword()))));
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        clientRepository.deleteById(employeeId);
    }

    @Override
    public Page<ClientDto> getMyEmployee(Pageable pageable) {
        Client client=securityContextUtil.getCurrentClient();
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),  pageable.getSort().isEmpty() ? Sort.by("createdDateTime").descending() : pageable.getSort());
        Page<Client> clientPage=clientRepository.findByBoss_Id(client.getId(),customPageable);
        List<ClientDto> clientDtoList=clientMapper.entityListToDtoList(clientPage.getContent());
        Page<ClientDto> clientDtoPage= new PageImpl<>(clientDtoList, clientPage.getPageable(), clientPage.getTotalElements());
        return clientDtoPage;
    }

    @Override
    public SavedAddressDto getMyDefaultAddress() {
        return savedAddressMapper.entityToDto(savedAddressService.findByIsDefaultAddress(Boolean.TRUE));
    }


}







