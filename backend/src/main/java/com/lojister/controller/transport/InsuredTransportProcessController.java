package com.lojister.controller.transport;

import com.lojister.core.util.annotation.Admin_InsuranceCompany;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.InsuredTransportProcessResponseDto;
import com.lojister.business.abstracts.InsuredTransportProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transportProcess/insured")
@CrossOrigin
@Authenticated
public class InsuredTransportProcessController {

    private final InsuredTransportProcessService insuredTransportProcessService;

    public InsuredTransportProcessController(InsuredTransportProcessService insuredTransportProcessService) {
        this.insuredTransportProcessService = insuredTransportProcessService;
    }

    @GetMapping
    @Admin_InsuranceCompany
    public ResponseEntity<List<InsuredTransportProcessResponseDto>> findWaiting() {
        return ResponseEntity.ok(insuredTransportProcessService.findWaiting());
    }


}
