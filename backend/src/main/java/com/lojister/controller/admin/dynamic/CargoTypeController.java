package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.CargoTypeDto;
import com.lojister.business.abstracts.dynamic.CargoTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/cargoType")
@CrossOrigin
@Authenticated
public class CargoTypeController {

    private final CargoTypeService cargoTypeService;

    public CargoTypeController(CargoTypeService cargoTypeService) {
        this.cargoTypeService = cargoTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(cargoTypeService.getById(id));

    }

    @GetMapping()
    @PermitAllCustom
    public ResponseEntity<List<CargoTypeDto>> getActive() {

        return ResponseEntity.ok(cargoTypeService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<CargoTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (showHidden) {
            return ResponseEntity.ok(cargoTypeService.getAll());
        } else {
            return ResponseEntity.ok(cargoTypeService.getActive());
        }
    }

    @PostMapping()
    public ResponseEntity<CargoTypeDto> save(@Valid @RequestBody CargoTypeDto cargoTypeDto) {

        return ResponseEntity.ok(cargoTypeService.save(cargoTypeDto));

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {
        cargoTypeService.deleteById(id);
    }
    /*

    @GetMapping("/active")
    public ResponseEntity<List<CargoTypeDto>> getActive() {

        return ResponseEntity.ok(cargoTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<CargoTypeDto>> getPassive() {

        return ResponseEntity.ok(cargoTypeService.getPassive());
    }

     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        cargoTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        cargoTypeService.hide(id);
    }


}
