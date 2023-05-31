package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.FrequentlyAskedQuestionsDto;
import com.lojister.business.abstracts.FrequentlyAskedQuestionsService;
import com.lojister.core.api.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frequentlyAskedQuestions")
@CrossOrigin
@Authenticated
public class FrequentlyAskedQuestionsController {

    private final FrequentlyAskedQuestionsService frequentlyAskedQuestionsService;

    public FrequentlyAskedQuestionsController(FrequentlyAskedQuestionsService frequentlyAskedQuestionsService) {
        this.frequentlyAskedQuestionsService = frequentlyAskedQuestionsService;
    }


    @GetMapping("/{id}")
    @PermitAllCustom
    public ResponseEntity<FrequentlyAskedQuestionsDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(frequentlyAskedQuestionsService.getById(id));

    }

    @GetMapping()
    @PermitAllCustom
    public ResponseEntity<List<FrequentlyAskedQuestionsDto>> getAll() {

        return ResponseEntity.ok(frequentlyAskedQuestionsService.getAll());
    }

    @PostMapping()
    public ResponseEntity<FrequentlyAskedQuestionsDto> save(@RequestBody FrequentlyAskedQuestionsDto frequentlyAskedQuestionsDto) {

        return ResponseEntity.ok(frequentlyAskedQuestionsService.save(frequentlyAskedQuestionsDto));

    }

    @PutMapping("/{id}")
    public ResponseEntity<FrequentlyAskedQuestionsDto> update(@PathVariable(name = "id") Long id, @RequestBody FrequentlyAskedQuestionsDto frequentlyAskedQuestionsDto) {

        return ResponseEntity.ok(frequentlyAskedQuestionsService.update(id, frequentlyAskedQuestionsDto));

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        frequentlyAskedQuestionsService.deleteById(id);

    }


}
