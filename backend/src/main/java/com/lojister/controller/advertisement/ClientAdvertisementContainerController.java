package com.lojister.controller.advertisement;

import com.lojister.business.abstracts.ClientAdvertisementContainerService;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementContainerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientAdvertisement/container")
@CrossOrigin
@Authenticated
@RequiredArgsConstructor
public class ClientAdvertisementContainerController {

    private final ClientAdvertisementContainerService clientAdvertisementContainerService;

    @PostMapping()
    public ClientAdvertisementContainerDto save(@RequestBody SaveClientAdvertisementContainerRequest saveClientAdvertisementContainerRequest){
       return clientAdvertisementContainerService.save(saveClientAdvertisementContainerRequest);
    }

}
