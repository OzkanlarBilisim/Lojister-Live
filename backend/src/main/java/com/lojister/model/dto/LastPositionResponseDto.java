package com.lojister.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lojister.model.enums.TransportProcessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastPositionResponseDto {

    private Double latitude;

    private Double longitude;

    private LocalDate logDate;

    private LocalTime logTime;

    private String licencePlate;

    private String transportCode;

    private Long transportId;

    private TransportProcessType transportProcessType;

    @JsonProperty("logLocalDateTime")
    private String getLogLocalDateTime(){
        if(Optional.ofNullable(logDate).isPresent()){
            if (Optional.ofNullable(logTime).isPresent()) {
                return LocalDateTime.of(logDate,logTime).toString();
            }
            else {
                return LocalDateTime.of(logDate,LocalTime.of(0,0,0)).toString();
            }
        }
        return "";
    }

}
