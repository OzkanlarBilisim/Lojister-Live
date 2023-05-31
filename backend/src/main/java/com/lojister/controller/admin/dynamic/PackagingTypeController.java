package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.PackagingTypeDto;
import com.lojister.business.abstracts.dynamic.PackagingTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/packagingType")
@CrossOrigin
@Authenticated
public class PackagingTypeController {

    private final PackagingTypeService packagingTypeService;

    public PackagingTypeController(PackagingTypeService packagingTypeService) {
        this.packagingTypeService = packagingTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackagingTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(packagingTypeService.getById(id));
    }

    @PermitAllCustom
    @GetMapping()
    public ResponseEntity<List<PackagingTypeDto>> getActive() {

        return ResponseEntity.ok(packagingTypeService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<PackagingTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(packagingTypeService.getAll());
        } else {
            return ResponseEntity.ok(packagingTypeService.getActive());
        }
    }

    @PostMapping()
    public ResponseEntity<PackagingTypeDto> save(@Valid @RequestBody PackagingTypeDto packagingTypeDto) {

        return ResponseEntity.ok(packagingTypeService.save(packagingTypeDto));

    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        packagingTypeService.deleteById(id);

    }

    /*
    @GetMapping("/active")
    public ResponseEntity<List<PackagingTypeDto>> getActive() {

        return ResponseEntity.ok(packagingTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<PackagingTypeDto>> getPassive() {

        return ResponseEntity.ok(packagingTypeService.getPassive());
    }


     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        packagingTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        packagingTypeService.hide(id);
    }

}
