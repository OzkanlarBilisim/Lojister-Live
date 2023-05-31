package com.lojister.core.util.sender;

import com.lojister.model.dto.ContactMailDto;
import com.lojister.core.util.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class ContactMailSender implements EmailSender<ContactMailDto> {

    private final JavaMailSender mailSender;

    @Value("${lojister.mail.info.toAddress}")
    private String toAddress;

    @Value("${lojister.mail.noReply.fromAddress}")
    private String fromAddress;



    @Override
    public void send(ContactMailDto contactMailDto) throws MessagingException, UnsupportedEncodingException {

        String senderName = "Lojister";
        String subject = "Lojister İletişim Maili";
        String content = " <b> Yeni bir mesajınız vardır: </b>  <br><br>"
                + "<b><ins> Mesajı yollayan ad soyad</ins> </b>  :  " + contactMailDto.getName() + "  <br><br>"
                + "<b><ins> Mesajın içeriği</ins> </b>  :  " + contactMailDto.getMessage() + " <br><br>"
                + "<b><ins> Mesaj yollayan email:</ins> </b>  " + contactMailDto.getEmail() + " <br><br>"
                + " <b> Lojister İletişim Sayfası </b>";


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


}
