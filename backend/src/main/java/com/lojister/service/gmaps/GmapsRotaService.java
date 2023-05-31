package com.lojister.service.gmaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.lojister.model.dto.map.LocationDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GmapsRotaService {
    String getGmapsRota(Long advertID) throws JsonProcessingException;
}
