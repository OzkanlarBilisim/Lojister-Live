package com.lojister.controller.advertisement;

import com.lojister.business.abstracts.ClientAdvertisementFtlService;
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
public class ClientAdvertisementFtlController {

    private final ClientAdvertisementFtlService clientAdvertisementFtlService;

    @PostMapping("/ftl")
    public void save(@Valid @RequestBody SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest) {
        clientAdvertisementFtlService.save(saveClientAdvertisementFtlRequest);
    }

}
