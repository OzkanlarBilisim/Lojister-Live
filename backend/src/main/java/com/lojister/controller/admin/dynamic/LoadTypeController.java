package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.LoadTypeDto;
import com.lojister.business.abstracts.dynamic.LoadTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loadType")
@CrossOrigin
@Authenticated
public class LoadTypeController {

    private final LoadTypeService loadTypeService;

    public LoadTypeController(LoadTypeService loadTypeService) {
        this.loadTypeService = loadTypeService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<LoadTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(loadTypeService.getById(id));

    }

    @GetMapping()
    @PermitAllCustom
    public ResponseEntity<List<LoadTypeDto>> getActive() {

        return ResponseEntity.ok(loadTypeService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<LoadTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(loadTypeService.getAll());
        } else {
            return ResponseEntity.ok(loadTypeService.getActive());
        }
    }

    @PostMapping()
    public ResponseEntity<LoadTypeDto> save(@Valid @RequestBody LoadTypeDto loadTypeDto) {

        return ResponseEntity.ok(loadTypeService.save(loadTypeDto));

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        loadTypeService.deleteById(id);

    }

    /*
    @GetMapping("/active")
    public ResponseEntity<List<LoadTypeDto>> getActive() {

        return ResponseEntity.ok(loadTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<LoadTypeDto>> getPassive() {

        return ResponseEntity.ok(loadTypeService.getPassive());
    }


     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        loadTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        loadTypeService.hide(id);
    }


}
