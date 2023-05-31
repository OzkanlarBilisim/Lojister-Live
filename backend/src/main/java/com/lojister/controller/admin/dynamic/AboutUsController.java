package com.lojister.controller.admin.dynamic;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.IsAdmin;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.dynamic.AboutUsDto;
import com.lojister.business.abstracts.dynamic.AboutUsService;
import com.lojister.core.api.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aboutUs")
@CrossOrigin
@Authenticated
public class AboutUsController {

    private final AboutUsService aboutUsService;

    @Autowired
    public AboutUsController(AboutUsService aboutUsService) {
        this.aboutUsService = aboutUsService;
    }

    @GetMapping()
    @PermitAllCustom
    public ResponseEntity<AboutUsDto> getAboutUs() {

        return ResponseEntity.ok(aboutUsService.getAboutUs());
    }

    @PutMapping("/tr")
    @OnlyAdmin
    public ResponseEntity<AboutUsDto> updateTr(@RequestBody AboutUsDto aboutUsDto) {

        return ResponseEntity.ok(aboutUsService.updateTr(aboutUsDto));

    }

    @PutMapping("/eng")
    @IsAdmin
    public ResponseEntity<AboutUsDto> updateEng(@RequestBody AboutUsDto aboutUsDto) {

        return ResponseEntity.ok(aboutUsService.updateEng(aboutUsDto));

    }


}
