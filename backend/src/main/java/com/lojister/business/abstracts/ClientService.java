package com.lojister.business.abstracts;

import com.lojister.controller.client.ClientDashboardResponse;
import com.lojister.controller.client.SaveClientEmployeeRequest;
import com.lojister.controller.client.UpdateClientAccountSettingRequest;
import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.model.dto.SavedAddressDto;
import com.lojister.model.dto.client.*;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.SavedAddress;
import com.lojister.model.entity.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    ClientUpdateResponseDto update(Long id, ClientUpdateRequestDto dto);

    ClientDto getById(Long id);

    void deleteById(Long id);

    List<ClientDto> getAll();

    Boolean changePassword(String oldPassword, String nwPassword);

    Client saveRepo(Client client);

    Client findDataById(Long id);

    void roleIsClientCheck(Client client);

    void unauthorizedTransactionCheck(Client client, Client currentClient);

    void updateClientEmailCheck(Client client, ClientUpdateRequestDto clientDto);

    void updateClientPhoneCheck(Client client, ClientUpdateRequestDto clientDto);

    ClientDashboardResponse getDashboardInformation();

    ClientNotificationSettingDto updateNotificationSetting(UpdateClientNotificationSettingRequest updateClientNotificationSettingRequest);

    ClientNotificationSettingDto getNotificationSetting();
    ClientAccountSettingDto updateAccountSetting(UpdateClientAccountSettingRequest updateClientAccountSettingRequest);

    ClientAccountSettingDto getAccountSetting();

    ClientDto saveEmployee(SaveClientEmployeeRequest saveClientEmployeeRequest);

    void deleteEmployee(Long employeeId);

    Page<ClientDto> getMyEmployee(Pageable pageable);

    SavedAddressDto getMyDefaultAddress();
}
