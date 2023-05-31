package com.lojister.core.util.sender;

import com.lojister.model.dto.SendMailToInsuranceCompanyForNewPaymentDto;
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
public class AfterPaymentToInsuranceMailSender implements EmailSender<SendMailToInsuranceCompanyForNewPaymentDto> {

    private final JavaMailSender mailSender;

    @Value("${lojister.insurance.company.mail}")
    private String toAddress;

    @Value("${lojister.mail.noReply.fromAddress}")
    private String fromAddress;


    @Override
    public void send(SendMailToInsuranceCompanyForNewPaymentDto dto) throws MessagingException, UnsupportedEncodingException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.FULL)
                .withZone(ZoneId.systemDefault());

        String senderName = "Lojister";
        String subject = "Lojister Yeni Sigorta Bildirimi";
        String content = " <b> Sistemde yeni bir ödeme yapılmıştır. </b>  <br><br>"
                + "<b><ins> İlanı Açanın Adı Soyadı : </ins> </b>    " + dto.getFirstName()
                + ", " + dto.getLastName() + "  <br><br>"
                + "<b><ins> Telefon Numarası: </ins> </b>    " + dto.getPhone() + " <br><br>"
                + "<b><ins> Mail Adresi: </ins> </b>    " + dto.getEmail() + " <br><br>"
                + "<b><ins> Var ise şirket ismi: </ins> </b>    " + dto.getCommercialTitle() + " <br><br>"
                + "<b><ins> Başlangıç Adresi: </ins> </b>    " + dto.getStartingAddress() + " <br><br>"
                + "<b><ins> Varış Adresi: </ins> </b>    " + dto.getDueAddress() + " <br><br>"
                + "<b><ins> Taşıma Kodu: </ins> </b>    " + dto.getTransportCode() + " <br><br>"
                + "<b><ins> Sigorta Tipi: </ins> </b>    " + dto.getInsuranceType() + " <br><br>"
                + "<b><ins> Mal Değeri: </ins> </b>    " + dto.getGoodsPrice() + " <br><br>"
                + "<b><ins> Hacim: </ins> </b>    " + dto.getVolume() + " <br><br>"
                + "<b><ins> Desi: </ins> </b>    " + dto.getDesi() + " <br><br>"
                + "<b><ins> Ldm: </ins> </b>    " + dto.getLdm() + " <br><br>"
                + "<b><ins> Tonaj: </ins> </b>    " + dto.getTonnage() + " <br><br>"
                + "<b><ins> Hammaliye var mı: </ins> </b>    " + dto.getIsPorter() + " <br><br>"
                + "<b><ins> İstifleme var mı: </ins> </b>    " + dto.getIsStacking() + " <br><br>"
                + "<b><ins> İlan açıklaması: </ins> </b>    " + dto.getExplanation() + " <br><br>"
                + "<b><ins> Doküman Numarası: </ins> </b>    " + dto.getDocumentNo() + " <br><br>"
                + "<b><ins> Yükseklik: </ins> </b>    " + dto.getHeight() + " <br><br>"
                + "<b><ins> Uzunluk: </ins> </b>    " + dto.getLength() + " <br><br>"
                + "<b><ins> En: </ins> </b>    " + dto.getWidth() + " <br><br>"
                + "<b><ins> İlan Tipi: </ins> </b>    " + dto.getClientAdvertisementType() + " <br><br>"
                + " <b> Lojister Yeni Ödeme Bildirimi. </b>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

}
