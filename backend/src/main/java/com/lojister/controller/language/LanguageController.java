package com.lojister.controller.language;

import com.lojister.model.enums.Language;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/language")
@CrossOrigin
public class LanguageController {

    @GetMapping("/support")
    private List<Language> getSupportLanguage(){
        return Arrays.asList(Language.values());
    }

}
