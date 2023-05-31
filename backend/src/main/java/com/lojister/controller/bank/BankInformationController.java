package com.lojister.controller.bank;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.BankInformationDto;
import com.lojister.business.abstracts.BankInformationService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankInformation")
@CrossOrigin
@Authenticated
public class BankInformationController {

    private final BankInformationService bankInformationService;

    public BankInformationController(BankInformationService bankInformationService) {
        this.bankInformationService = bankInformationService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<BankInformationDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(bankInformationService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<BankInformationDto> save(@RequestBody BankInformationDto bankInformationDto) {

        return ResponseEntity.ok(bankInformationService.save(bankInformationDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankInformationDto> update(@PathVariable(name = "id") Long id, @RequestBody BankInformationDto bankInformationDto) {

        return ResponseEntity.ok(bankInformationService.update(id, bankInformationDto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        bankInformationService.deleteById(id);
    }

    //TODO GET ALL SONRA YAZILACAKTIR.

}
