package com.lojister.controller.company;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.IsAdmin;
import com.lojister.model.dto.SaveInsuranceCompanyDto;
import com.lojister.business.abstracts.InsuranceCompanyService;
import com.lojister.core.api.ApiPaths;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/insuranceCompany")
@CrossOrigin
@Authenticated
public class InsuranceCompanyController {

    private final InsuranceCompanyService insuranceCompanyService;

    public InsuranceCompanyController(InsuranceCompanyService insuranceCompanyService) {
        this.insuranceCompanyService = insuranceCompanyService;
    }

    @PostMapping
    @IsAdmin
    public void saveInsuranceCompanyUser(@Valid @RequestBody SaveInsuranceCompanyDto saveInsuranceCompanyDto) {
        insuranceCompanyService.save(saveInsuranceCompanyDto);
    }


}
