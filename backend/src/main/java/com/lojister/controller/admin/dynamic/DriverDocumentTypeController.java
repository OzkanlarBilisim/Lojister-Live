package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.dynamic.DriverDocumentTypeDto;
import com.lojister.business.abstracts.dynamic.DriverDocumentTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/driverDocumentType")
@CrossOrigin
@Authenticated
public class DriverDocumentTypeController {

    private final DriverDocumentTypeService driverDocumentTypeService;

    public DriverDocumentTypeController(DriverDocumentTypeService driverDocumentTypeService) {
        this.driverDocumentTypeService = driverDocumentTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDocumentTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(driverDocumentTypeService.getById(id));

    }

    @GetMapping()
    public ResponseEntity<List<DriverDocumentTypeDto>> getActive() {

        return ResponseEntity.ok(driverDocumentTypeService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<DriverDocumentTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(driverDocumentTypeService.getAll());
        } else {
            return ResponseEntity.ok(driverDocumentTypeService.getActive());
        }
    }

    @PostMapping()
    public ResponseEntity<DriverDocumentTypeDto> save(@Valid @RequestBody DriverDocumentTypeDto driverDocumentTypeDto) {

        return ResponseEntity.ok(driverDocumentTypeService.save(driverDocumentTypeDto));

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        driverDocumentTypeService.deleteById(id);

    }

    /*
    @GetMapping("/active")
    public ResponseEntity<List<DriverDocumentTypeDto>> getActive() {

        return ResponseEntity.ok(driverDocumentTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<DriverDocumentTypeDto>> getPassive() {

        return ResponseEntity.ok(driverDocumentTypeService.getPassive());
    }

     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        driverDocumentTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        driverDocumentTypeService.hide(id);
    }


}
