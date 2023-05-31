package com.lojister.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Data
public class Position {

    private Double latitude = 0D;

    private Double longitude = 0D;

    private LocalDate logDate = LocalDate.now();

    private LocalTime logTime = LocalTime.now();

}
