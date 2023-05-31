package com.lojister.controller.transport;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.PayDriverDto;
import com.lojister.model.dto.TransportPaymentDetailDto;
import com.lojister.model.dto.TransportPaymentDto;
import com.lojister.business.abstracts.TransportPaymentService;
import com.lojister.core.api.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportPayment")
@CrossOrigin
@Authenticated
public class TransportPaymentController {

    private final TransportPaymentService paymentService;

    @Autowired
    public TransportPaymentController(TransportPaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping("/transportProcess/{transportProcessId}")
    public ResponseEntity<TransportPaymentDto> getByTransportProcessId(@PathVariable(value = "transportProcessId") Long transportProcessId) {

        return ResponseEntity.ok(paymentService.getByTransportProcessId(transportProcessId));
    }

    @GetMapping("/transportProcess/detail")
    public ResponseEntity<TransportPaymentDetailDto> getDetailByTransportProcessId(@RequestParam(value = "transportCode") String transportCode) {

        return ResponseEntity.ok(paymentService.getTransportProcessDetailByTransportCodeForTransportPayment(transportCode));
    }

    @PostMapping
    public void payTheDriver(@RequestBody PayDriverDto payDriverDto) {

        paymentService.payTheDriver(payDriverDto);

    }

    @GetMapping()
    public ResponseEntity<List<TransportPaymentDto>> getAll() {

        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/paymentWaiting")
    public ResponseEntity<List<TransportPaymentDto>> getPaymentWaiting() {

        return ResponseEntity.ok(paymentService.getPaymentWaiting());
    }

    @GetMapping("/paymentCompleted")
    public ResponseEntity<List<TransportPaymentDto>> getPaymentCompleted() {

        return ResponseEntity.ok(paymentService.getPaymentCompleted());
    }


}
