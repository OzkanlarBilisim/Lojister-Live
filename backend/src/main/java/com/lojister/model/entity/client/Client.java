package com.lojister.model.entity.client;

import com.lojister.controller.client.SaveClientEmployeeRequest;
import com.lojister.controller.client.UpdateClientAccountSettingRequest;
import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.model.dto.addresses.AddressesDto;
import com.lojister.model.entity.Address;
import com.lojister.model.entity.SavedAddress;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.entity.driver.DriverNotificationSetting;
import com.lojister.model.enums.ClientTitle;
import com.lojister.model.enums.ClientType;
import com.lojister.model.entity.Company;
import com.lojister.model.entity.User;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.BeanDefinitionDsl;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@Setter
@DiscriminatorValue("Client")
public class Client extends User {

    //Company boşsa şahıstır.
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String citizenId;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToOne
    @JoinColumn(name = "boss_id")
    private Client boss;

    @Enumerated(EnumType.STRING)
    private ClientTitle clientTitle;
    @Embedded
    private ClientNotificationSetting notificationSetting;

    @Embedded
    private ClientAccountSetting accountSetting;

    private Boolean current;
    private Double usd;
    private Double tl;
    private Double euro;

    public Client updateNotificationSetting(UpdateClientNotificationSettingRequest updateClientNotificationSettingRequest) {
        if (Optional.ofNullable(notificationSetting).isPresent()) {
            this.notificationSetting.update(updateClientNotificationSettingRequest);
        } else {
            ClientNotificationSetting clientNotificationSetting = new ClientNotificationSetting();
            clientNotificationSetting.update(updateClientNotificationSettingRequest);
            notificationSetting = clientNotificationSetting;
        }
        return this;
    }

    public Client updateAccountSetting(UpdateClientAccountSettingRequest updateClientAccountSettingRequest) {
        if (Optional.ofNullable(accountSetting).isPresent()) {
            this.accountSetting.update(updateClientAccountSettingRequest);
        } else {
            ClientAccountSetting clientAccountSetting = new ClientAccountSetting();
            clientAccountSetting.update(updateClientAccountSettingRequest);
            accountSetting = clientAccountSetting;
        }
        return this;
    }

    public static Client saveEmployee(Client client, SaveClientEmployeeRequest saveClientEmployeeRequest,String encodePassword) {
        Client employee = new Client();

        employee.setFirstName(saveClientEmployeeRequest.getFirstName());
        employee.setLastName(saveClientEmployeeRequest.getLastName());
        employee.setPhone(saveClientEmployeeRequest.getPhone());
        employee.setEmail(saveClientEmployeeRequest.getEmail());
        employee.setPassword(encodePassword);
        employee.setBoss(client);
        employee.setClientType(client.getClientType());
        employee.setRole(Role.ROLE_CLIENT_EMPLOYEE);
        employee.setClientTitle(ClientTitle.EMPLOYEE);
        return employee;
    }

}
