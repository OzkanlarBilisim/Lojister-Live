package com.lojister.controller.company;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.request.CommissionRateDto;
import com.lojister.model.dto.CompanyDashboardRatingCountDto;
import com.lojister.model.dto.CompanyDto;
import com.lojister.business.abstracts.CompanyService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/company")
@CrossOrigin
@Authenticated
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @GetMapping("/myCompany")
    public ResponseEntity<CompanyDto> getMyCompany() {

        return ResponseEntity.ok(companyService.getMyCompany());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(companyService.getById(id));

    }

    @GetMapping()
    public ResponseEntity<List<CompanyDto>> getAll() {

        return ResponseEntity.ok(companyService.getAll());
    }

    @PutMapping()
    public ResponseEntity<CompanyDto> update(@RequestBody CompanyDto companyDto) {

        return ResponseEntity.ok(companyService.update(companyDto));

    }

    @PutMapping("/commissionRate")
    public ResponseEntity<CompanyDto> updateCommissionRate(@Valid @RequestBody CommissionRateDto commissionRateDto) {

        return ResponseEntity.ok(companyService.updateCommissionRate(commissionRateDto));

    }

    @GetMapping("/findByCommercialTitle")
    public ResponseEntity<List<CompanyDto>> findByCommercialTitleContains(@RequestParam String commercialTitle) {

        return ResponseEntity.ok(companyService.findByCommercialTitleContainsOrderByIdDesc(commercialTitle));

    }

    @GetMapping("/ratingInformation")
    public ResponseEntity<CompanyDashboardRatingCountDto> getDashboardRatingCountInformation() {

        return ResponseEntity.ok(companyService.getDashboardRatingCountInformation());
    }


}
