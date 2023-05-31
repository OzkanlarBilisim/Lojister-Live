package com.lojister.business.concretes;

import com.lojister.model.dto.ContactMailDto;
import com.lojister.business.abstracts.ContactService;
import com.lojister.core.util.sender.ContactMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactMailSender contactMailSender;

    @Override
    public Boolean sendMail(ContactMailDto contactMailDto) throws MessagingException, UnsupportedEncodingException {

        contactMailSender.send(contactMailDto);
        return true;

    }
}
