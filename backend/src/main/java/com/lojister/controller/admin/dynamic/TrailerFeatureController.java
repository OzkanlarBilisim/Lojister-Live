package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.TrailerFeatureDto;
import com.lojister.business.abstracts.dynamic.TrailerFeatureService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trailerFeature")
@CrossOrigin
@Authenticated
public class TrailerFeatureController {

    private final TrailerFeatureService trailerFeatureService;

    public TrailerFeatureController(TrailerFeatureService trailerFeatureService) {
        this.trailerFeatureService = trailerFeatureService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TrailerFeatureDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(trailerFeatureService.getById(id));

    }

    @PermitAllCustom
    @GetMapping()
    public ResponseEntity<List<TrailerFeatureDto>> getActive() {

        return ResponseEntity.ok(trailerFeatureService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<TrailerFeatureDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(trailerFeatureService.getAll());
        } else {
            return ResponseEntity.ok(trailerFeatureService.getActive());
        }

    }

    @PostMapping()
    public ResponseEntity<TrailerFeatureDto> save(@Valid @RequestBody TrailerFeatureDto trailerFeatureDto) {

        return ResponseEntity.ok(trailerFeatureService.save(trailerFeatureDto));

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        trailerFeatureService.deleteById(id);
    }

    /*
    @GetMapping("/active")
    public ResponseEntity<List<TrailerFeatureDto>> getActive() {

        return ResponseEntity.ok(trailerFeatureService.getActive());
    }

    @GetMapping("/passive")
    public ResponseEntity<List<TrailerFeatureDto>> getPassive() {

        return ResponseEntity.ok(trailerFeatureService.getPassive());
    }


     */

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable("id") Long id) {

        trailerFeatureService.activate(id);
    }

    @GetMapping("/{id}/hide")
    public void hide(@PathVariable("id") Long id) {

        trailerFeatureService.hide(id);
    }


}
