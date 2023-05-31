package com.lojister.core.util.sender;

import com.lojister.core.i18n.Translator;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.dto.SendMailConfirmationTransportDto;
import com.lojister.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@RequiredArgsConstructor
public class ConfirmationTokenMailSender {
    private final JavaMailSender mailSender;
    @Value("${lojister.mail.noReply.fromAddress}")
    private String fromAddress;
    @Value("${lojister.confirmationToken.url}")
    private String url;
     private final TemplateEngine templateEngine;

  public enum Status{
    STARTING,
    ENDING
}

    public void send(SendMailConfirmationTransportDto sendMailConfirmationTransportDto,Status status) throws MessagingException, UnsupportedEncodingException {

        String senderName = Translator.toLocale("lojister.common.senderName");
        String subject = Translator.toLocale("lojister.acceptTransport.subject");
        String mailMessage =Translator.toLocale("lojister.acceptTransport.mailMessage");
        String buttonText = Translator.toLocale("lojister.acceptTransport.buttonText");
        String contact =Translator.toLocale("lojister.acceptTransport.contact");
        String address =Translator.toLocale("lojister.acceptTransport.address");

        String buttonUrl=url+"?transportCode="+sendMailConfirmationTransportDto.getTransportCode()+"&token="+sendMailConfirmationTransportDto.getToken()+"&type="+status;
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("buttonUrl", buttonUrl);
        ctx.setVariable("title",status==Status.STARTING?Translator.toLocale("lojister.acceptTransport.title.starting"):Translator.toLocale("lojister.acceptTransport.title.ending"));
        ctx.setVariable("message",mailMessage);
        ctx.setVariable("buttonText",buttonText);
        ctx.setVariable("contact",contact);
        ctx.setVariable("address",address);
        final String htmlContent = this.templateEngine.process("accept_transport", ctx);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(sendMailConfirmationTransportDto.getEmail());
        helper.setSubject(subject);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
