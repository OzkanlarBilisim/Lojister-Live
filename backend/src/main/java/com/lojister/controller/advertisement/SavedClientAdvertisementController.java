package com.lojister.controller.advertisement;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.SavedClientAdvertisementResponseDto;
import com.lojister.model.dto.SavedClientAdvertisementSaveDto;
import com.lojister.business.abstracts.SavedClientAdvertisementService;
import com.lojister.core.api.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/savedClientAdvertisement")
@CrossOrigin
@Authenticated
@RequiredArgsConstructor
public class SavedClientAdvertisementController {

    private final SavedClientAdvertisementService savedClientAdvertisementService;

    @PostMapping()
    public void save(@RequestBody SavedClientAdvertisementSaveDto savedClientAdvertisementSaveDto) {

        savedClientAdvertisementService.save(savedClientAdvertisementSaveDto);

    }


    @GetMapping("/{savedClientAdvertisementId}")
    public ResponseEntity<SavedClientAdvertisementResponseDto> getById(@PathVariable(value = "savedClientAdvertisementId") Long savedClientAdvertisementId) {

        return ResponseEntity.ok(savedClientAdvertisementService.getById(savedClientAdvertisementId));

    }


    @DeleteMapping("/{savedClientAdvertisementId}")
    public void deleteById(@PathVariable(value = "savedClientAdvertisementId") Long savedClientAdvertisementId) {

        savedClientAdvertisementService.deleteById(savedClientAdvertisementId);
    }


    @GetMapping
    public ResponseEntity<List<SavedClientAdvertisementResponseDto>> getMySavedClientAdvertisements() {

        return ResponseEntity.ok(savedClientAdvertisementService.getMySavedClientAdvertisements());

    }


}
