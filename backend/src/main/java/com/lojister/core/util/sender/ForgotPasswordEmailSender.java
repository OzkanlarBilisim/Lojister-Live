package com.lojister.core.util.sender;

import com.lojister.model.dto.ForgotMyPasswordMailDto;
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
public class ForgotPasswordEmailSender implements EmailSender<ForgotMyPasswordMailDto> {

    private final JavaMailSender mailSender;

    @Value("${lojister.mail.noReply.fromAddress}")
    private String fromAddress;

    @Value("${lojister.site.url}")
    private String siteUrl;
    @Override
    public void send(ForgotMyPasswordMailDto forgotMyPasswordMailDto) throws MessagingException, UnsupportedEncodingException {

        String toAddress = forgotMyPasswordMailDto.getEmail();
        String senderName = "Lojister";
        String subject = "Lojister için Parola Sıfırlama İsteği";
        String content = "Merhaba [[name]],<br>"
                + "Şifrenizi sıfırlamak için lütfen linke tıklayınız. Yeni bir şifre istemediyseniz lütfen bu e-postayı görmezden geliniz. <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">SIFIRLA</a></h3>"
                + "Teşekkürler,<br>"
                + "Lojister.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", forgotMyPasswordMailDto.getFirstName() + " " + forgotMyPasswordMailDto.getLastName());
        String verifyURL = siteUrl + "/login?resetCode=" + forgotMyPasswordMailDto.getVerificationToken();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }


}
