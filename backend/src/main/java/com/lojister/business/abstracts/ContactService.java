package com.lojister.business.abstracts;

import com.lojister.model.dto.ContactMailDto;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface ContactService {

    Boolean sendMail(ContactMailDto contactMailDto) throws MessagingException, UnsupportedEncodingException;

}
