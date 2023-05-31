package com.lojister.controller.api;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.core.api.ApiPaths;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping()
@CrossOrigin
@Authenticated
public class RootController {

    @GetMapping
    @PermitAllCustom
    public String index() {
        return "Lojister-API";
    }

    @GetMapping("/dt")
    @PermitAllCustom
    public String dt() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
