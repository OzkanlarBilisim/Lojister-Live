package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.IsAdmin;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.CurrencyUnitDto;
import com.lojister.business.abstracts.dynamic.CurrencyUnitService;
import com.lojister.core.api.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/currencyUnit")
@CrossOrigin
@Authenticated
public class CurrencyUnitController {


    private final CurrencyUnitService currencyUnitService;

    @Autowired
    public CurrencyUnitController(CurrencyUnitService currencyUnitService) {
        this.currencyUnitService = currencyUnitService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<CurrencyUnitDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(currencyUnitService.getById(id));

    }

    @PermitAllCustom
    @GetMapping()
    public ResponseEntity<List<CurrencyUnitDto>> getActive() {

        return ResponseEntity.ok(currencyUnitService.getActive());
    }


    @GetMapping(params = "showHidden")
    public ResponseEntity<List<CurrencyUnitDto>> getAll(@RequestParam Boolean showHidden) {

        if (Boolean.TRUE.equals(showHidden)) {
            return ResponseEntity.ok(currencyUnitService.getAll());
        } else {
            return ResponseEntity.ok(currencyUnitService.getActive());
        }
    }

    @PostMapping()
    @IsAdmin
    public ResponseEntity<CurrencyUnitDto> save(@Valid @RequestBody CurrencyUnitDto currencyUnitDto) {

        return ResponseEntity.ok(currencyUnitService.save(currencyUnitDto));

    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public void deleteById(@PathVariable(name = "id") Long id) {

        currencyUnitService.deleteById(id);

    }


    @GetMapping("/{id}/activate")
    @IsAdmin
    public void activate(@PathVariable("id") Long id) {

        currencyUnitService.activate(id);
    }

    @GetMapping("/{id}/hide")
    @IsAdmin
    public void hide(@PathVariable("id") Long id) {

        currencyUnitService.hide(id);
    }


}
