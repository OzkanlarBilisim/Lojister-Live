package com.lojister.service.gmaps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojister.model.dto.map.LocationDTO;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.repository.advertisement.ClientAdvertisementRepository;
import com.lojister.repository.client.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GmapsRotaServicelmpl implements GmapsRotaService {
    private final ClientRepository clientRepository;
    private final ClientAdvertisementRepository clientAdvertisementRepository;

    public GmapsRotaServicelmpl(ClientRepository clientRepository,
                                ClientAdvertisementRepository clientAdvertisementRepository) {
        this.clientRepository = clientRepository;
        this.clientAdvertisementRepository = clientAdvertisementRepository;
    }

    public String getGmapsRota(Long advertID) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.mapbox.com/directions/v5/mapbox/driving/29.085837%2C37.766384%3B27.761700723802193%2C38.30908328456246.json?geometries=polyline&overview=full&access_token=pk.eyJ1IjoiaGxvb3oiLCJhIjoiY2xnZGtlYXluMGFuODNnbWZpcXJndHZ0eCJ9.dgdDhxr9f6HBjWNngwGKoA&steps=true";
        String result = restTemplate.getForObject(url, String.class);
        return result;

    }
}