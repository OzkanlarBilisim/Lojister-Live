package com.lojister.controller.Adresses;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.SavedAddressDto;
import com.lojister.business.abstracts.SavedAddressService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/savedAddress")
@CrossOrigin
@Authenticated
public class SavedAddressController {

    private final SavedAddressService savedAddressService;

    public SavedAddressController(SavedAddressService savedAddressService) {
        this.savedAddressService = savedAddressService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<SavedAddressDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(savedAddressService.getById(id));
    }

    @GetMapping("/myAddresses")
    public ResponseEntity<List<SavedAddressDto>> getMyAddresses() {

        return ResponseEntity.ok(savedAddressService.myAddresses());
    }

    @PostMapping()
    public ResponseEntity<SavedAddressDto> save(@Valid @RequestBody SavedAddressDto savedAddressDto) {

        return ResponseEntity.ok(savedAddressService.save(savedAddressDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavedAddressDto> update(@PathVariable(name = "id") Long id, @Valid @RequestBody SavedAddressDto savedAddressDto) {

        return ResponseEntity.ok(savedAddressService.update(id, savedAddressDto));
    }

    @PutMapping("/{id}/defaultAddress")
    public List<SavedAddressDto> updateDefaultAddress(@PathVariable(name = "id") Long id) {
        return savedAddressService.updateDefaultAddress(id);
    }

    @PutMapping("/defaultAddress/disable")
    public void disableDefaultAddress(){
        savedAddressService.disableDefaultAddress();
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        savedAddressService.deleteById(id);
    }


}
