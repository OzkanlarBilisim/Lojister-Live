package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.TrailerFloorTypeDto;
import com.lojister.business.abstracts.dynamic.TrailerFloorTypeService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trailerFloorType")
@CrossOrigin
@Authenticated
public class TrailerFloorTypeController {

    private final TrailerFloorTypeService trailerFloorTypeService;

    public TrailerFloorTypeController(TrailerFloorTypeService trailerFloorTypeService) {
        this.trailerFloorTypeService = trailerFloorTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrailerFloorTypeDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(trailerFloorTypeService.getById(id));

    }

    @PermitAllCustom
    @GetMapping()
    public ResponseEntity<List<TrailerFloorTypeDto>> getActive() {

        return ResponseEntity.ok(trailerFloorTypeService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<TrailerFloorTypeDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(trailerFloorTypeService.getAll());
        } else {
            return ResponseEntity.ok(trailerFloorTypeService.getActive());
        }

    }

    @PostMapping()
    public ResponseEntity<TrailerFloorTypeDto> save(@Valid @RequestBody TrailerFloorTypeDto trailerFloorTypeDto) {

        return ResponseEntity.ok(trailerFloorTypeService.save(trailerFloorTypeDto));

    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        trailerFloorTypeService.deleteById(id);

    }

    /*
    @GetMapping("/active")
    public ResponseEntity<List<TrailerFloorTypeDto>> getActive() {

        return ResponseEntity.ok(trailerFloorTypeService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<TrailerFloorTypeDto>> getPassive() {

        return ResponseEntity.ok(trailerFloorTypeService.getPassive());
    }


     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        trailerFloorTypeService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        trailerFloorTypeService.hide(id);
    }


}
