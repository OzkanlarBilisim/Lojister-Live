package com.lojister.core.util.sender;

import com.lojister.model.dto.NewAdvertisementMailNotificationDto;
import com.lojister.core.util.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@RequiredArgsConstructor
public class NewAdvertisementAdminMailSender implements EmailSender<NewAdvertisementMailNotificationDto> {

    private final JavaMailSender mailSender;

    @Value("${lojister.mail.info.toAddress}")
    private String toAddress;

    @Value("${lojister.mail.noReply.fromAddress}")
    private String fromAddress;

    @Override
    public void send(NewAdvertisementMailNotificationDto newAdvertisementMailNotificationDto) throws MessagingException, UnsupportedEncodingException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.FULL)
                .withZone(ZoneId.systemDefault());

        String senderName = "Lojister";
        String subject = "Lojister Yeni İlan Bildirimi";
        String content = " <b> Sistemde yeni bir ilan verilmiştir. </b>  <br><br>"
                + "<b><ins> İlanı Açanın Adı Soyadı : </ins> </b>    " + newAdvertisementMailNotificationDto.getFirstName()
                + ", " + newAdvertisementMailNotificationDto.getLastName() + "  <br><br>"
                + "<b><ins> Başlangıç Adresi: </ins> </b>    " + newAdvertisementMailNotificationDto.getStartProvince()
                + ", " + newAdvertisementMailNotificationDto.getStartDistrict()
                + ", " + newAdvertisementMailNotificationDto.getStartNeighborhood() + " <br><br>"
                + "<b><ins> Varış Adresi: </ins> </b>    " + newAdvertisementMailNotificationDto.getDueProvince()
                + ", " + newAdvertisementMailNotificationDto.getDueDistrict()
                + ", " + newAdvertisementMailNotificationDto.getDueNeighborhood() + " <br><br>"
                + "<b><ins> İlanın Açılma Tarihi :</ins> </b>  " + dateTimeFormatter.format(newAdvertisementMailNotificationDto.getAdvertisementDate()) + " <br><br>"
                + " <b> Lojister Yeni İlan Bildirimi. </b>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);


    }
}
