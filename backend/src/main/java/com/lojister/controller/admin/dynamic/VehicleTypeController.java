package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.VehicleTypeDto;
import com.lojister.business.abstracts.dynamic.VehicleTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicleType")
@CrossOrigin
@Authenticated
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    public VehicleTypeController(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<VehicleTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(vehicleTypeService.getById(id));

    }
    @PermitAllCustom
    @GetMapping()
    public ResponseEntity<List<VehicleTypeDto>> getActive() {

        return ResponseEntity.ok(vehicleTypeService.getActive());
    }

    @GetMapping(params = "showHidden")
    public ResponseEntity<List<VehicleTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(vehicleTypeService.getAll());
        } else {
            return ResponseEntity.ok(vehicleTypeService.getActive());
        }


    }

    @PostMapping()
    public ResponseEntity<VehicleTypeDto> save(@Valid @RequestBody VehicleTypeDto vehicleTypeDto) {

        return ResponseEntity.ok(vehicleTypeService.save(vehicleTypeDto));

    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        vehicleTypeService.deleteById(id);

    }
/*
    @GetMapping("/active")
    public ResponseEntity<List<VehicleTypeDto>> getActive() {

        return ResponseEntity.ok(vehicleTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<VehicleTypeDto>> getPassive() {

        return ResponseEntity.ok(vehicleTypeService.getPassive());
    }

 */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        vehicleTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        vehicleTypeService.hide(id);
    }

}
