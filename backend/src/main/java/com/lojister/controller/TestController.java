package com.lojister.controller;

import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.enums.AdvertisementProcessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final ClientAdvertisementService clientAdvertisementService;

    /*@PutMapping("advertisementProcessStatus/{id}/update/{advertisementProcessStatus}")
    public void updateClientAdvertisementProcessStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "advertisementProcessStatus") AdvertisementProcessStatus advertisementProcessStatus){
        clientAdvertisementService.updateClientAdvertisementProcessStatus(id,advertisementProcessStatus);
    }
*/

}
