package com.lojister.model.entity.driver;

import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.controller.driver.UpdateDriverNotificationSettingRequest;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAccountSetting;
import com.lojister.model.entity.client.ClientNotificationSetting;
import com.lojister.model.enums.DriverStatus;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.entity.Company;
import com.lojister.model.entity.Position;
import com.lojister.model.entity.User;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Optional;

@Data
@Entity
@DiscriminatorValue("Driver")
public class Driver extends User {

    @Enumerated(EnumType.STRING)
    private DriverTitle driverTitle;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DriverStatus status;

    @ColumnDefault("''")
    private String statusDescription = "";

    @Column(unique = true)
    private String citizenId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    //Boss doluysa, driver birisinin altındadır.
    @ManyToOne
    @JoinColumn(name = "boss_id")
    private Driver boss;

    private Float rating = null;

    @Embedded
    private Position lastPosition;

    @Embedded
    private DriverNotificationSetting notificationSetting;


    public Driver updateNotificationSetting(UpdateDriverNotificationSettingRequest updateDriverNotificationSettingRequest){
        if(Optional.ofNullable(notificationSetting).isPresent()){
            this.notificationSetting.update(updateDriverNotificationSettingRequest);
        }
        else {
            DriverNotificationSetting driverNotificationSetting = new DriverNotificationSetting();
            driverNotificationSetting.update(updateDriverNotificationSettingRequest);
            notificationSetting=driverNotificationSetting;
        }

        return this;
    }
}
