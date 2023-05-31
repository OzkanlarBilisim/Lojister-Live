package com.lojister.controller.admin.dynamic;


import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.dynamic.VehicleDocumentTypeDto;
import com.lojister.business.abstracts.dynamic.VehicleDocumentTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicleDocumentType")
@CrossOrigin
@Authenticated
public class VehicleDocumentTypeController {

    private final VehicleDocumentTypeService vehicleDocumentTypeService;

    public VehicleDocumentTypeController(VehicleDocumentTypeService vehicleDocumentTypeService) {
        this.vehicleDocumentTypeService = vehicleDocumentTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDocumentTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(vehicleDocumentTypeService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<VehicleDocumentTypeDto>> getActive() {

        return ResponseEntity.ok(vehicleDocumentTypeService.getActive());
    }

    @GetMapping(params = "showHidden")
    public ResponseEntity<List<VehicleDocumentTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(vehicleDocumentTypeService.getAll());
        } else {
            return ResponseEntity.ok(vehicleDocumentTypeService.getActive());
        }
    }


    @PostMapping()
    public ResponseEntity<VehicleDocumentTypeDto> save(@Valid @RequestBody VehicleDocumentTypeDto vehicleDocumentTypeDto) {

        return ResponseEntity.ok(vehicleDocumentTypeService.save(vehicleDocumentTypeDto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        vehicleDocumentTypeService.deleteById(id);

    }

    /*
    @GetMapping("/active")
    public ResponseEntity<List<VehicleDocumentTypeDto>> getActive() {

        return ResponseEntity.ok(vehicleDocumentTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<VehicleDocumentTypeDto>> getPassive() {

        return ResponseEntity.ok(vehicleDocumentTypeService.getPassive());
    }
     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {
        vehicleDocumentTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {
        vehicleDocumentTypeService.hide(id);
    }


}



