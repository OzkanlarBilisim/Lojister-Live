package com.lojister.controller.map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.PositionDto;
import com.lojister.model.dto.map.LocationDTO;
import com.lojister.service.gmaps.GmapsRotaService;

import com.lojister.service.osrm.model.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map")
@CrossOrigin
@RequiredArgsConstructor
public class MapController {

   /* private final OsrmService osrmService;
    @Autowired
    private GmapsRotaService gmapsRotaService;*/

    /*@PostMapping("/getRoute")
    public List<LatLng> getRoute(@RequestBody RouteRequest routeRequest) {

       return osrmService.route(routeRequest.getStartingLng(), routeRequest.getStartingLat(), routeRequest.getEndingLng(), routeRequest.getEndingLat());

    }
    @GetMapping("/rota/{advertID}")
    public String getGmapsRota(@PathVariable Long advertID) throws JsonProcessingException {
        return gmapsRotaService.getGmapsRota(advertID);
    }*/
}
