package com.lojister.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewAdvertisementMailNotificationDto {

    private String firstName;
    private String lastName;
    private String startProvince;
    private String startDistrict;
    private String startNeighborhood;
    private String dueProvince;
    private String dueDistrict;
    private String dueNeighborhood;
    private LocalDateTime advertisementDate;

}
