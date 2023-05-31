package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.TrailerTypeDto;
import com.lojister.business.abstracts.dynamic.TrailerTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trailerType")
@CrossOrigin
@Authenticated
public class TrailerTypeController {

    private final TrailerTypeService trailerTypeService;

    public TrailerTypeController(TrailerTypeService trailerTypeService) {
        this.trailerTypeService = trailerTypeService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TrailerTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(trailerTypeService.getById(id));
    }

    @PermitAllCustom
    @GetMapping()
    public ResponseEntity<List<TrailerTypeDto>> getActive() {

        return ResponseEntity.ok(trailerTypeService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<TrailerTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(trailerTypeService.getAll());
        } else {
            return ResponseEntity.ok(trailerTypeService.getActive());
        }
    }


    @PostMapping()
    public ResponseEntity<TrailerTypeDto> save(@Valid @RequestBody TrailerTypeDto trailerTypeDto) {

        return ResponseEntity.ok(trailerTypeService.save(trailerTypeDto));
    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        trailerTypeService.deleteById(id);
    }

    /*

    @GetMapping("/active")
    public ResponseEntity<List<TrailerTypeDto>> getActive() {

        return ResponseEntity.ok(trailerTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<TrailerTypeDto>> getPassive() {

        return ResponseEntity.ok(trailerTypeService.getPassive());
    }

     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        trailerTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        trailerTypeService.hide(id);
    }

}
