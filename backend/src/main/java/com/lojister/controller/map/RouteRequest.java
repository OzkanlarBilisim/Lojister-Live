package com.lojister.controller.map;

import lombok.Data;

@Data
public class RouteRequest {
    private Double startingLng;
    private Double startingLat;
    private Double endingLng;
    private Double endingLat;

}
