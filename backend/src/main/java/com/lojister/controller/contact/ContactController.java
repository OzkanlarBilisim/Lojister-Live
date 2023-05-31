package com.lojister.controller.contact;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.ContactMailDto;
import com.lojister.business.abstracts.ContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/contact")
@CrossOrigin
@Authenticated
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    @PostMapping("/sendMail")
    @PermitAllCustom
    public ResponseEntity<Boolean> sendContactMail(@Valid @RequestBody ContactMailDto contactMailDto) throws MessagingException, UnsupportedEncodingException {

        return ResponseEntity.ok(contactService.sendMail(contactMailDto));

    }


}
