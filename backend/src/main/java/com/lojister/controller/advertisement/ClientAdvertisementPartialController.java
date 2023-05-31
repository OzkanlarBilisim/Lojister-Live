package com.lojister.controller.advertisement;

import com.lojister.business.abstracts.ClientAdvertisementPartialService;
import com.lojister.core.api.ApiPaths;
import com.lojister.core.util.annotation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@RestController
@RequestMapping("/clientAdvertisement")
@CrossOrigin
@Authenticated
@RequiredArgsConstructor
public class ClientAdvertisementPartialController {
    private final ClientAdvertisementPartialService clientAdvertisementPartialService;

    @PostMapping("/partial")
    public void save(@Valid @RequestBody SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest) {
        clientAdvertisementPartialService.save(saveClientAdvertisementPartialRequest);
    }

}
