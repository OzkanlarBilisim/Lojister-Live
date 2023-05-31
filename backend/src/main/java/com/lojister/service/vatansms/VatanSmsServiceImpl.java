package com.lojister.service.vatansms;

import com.lojister.core.exception.SmsException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;


/**
 * @author Fatih Mayuk
 * @version %I%, %G%
 * @since 1.0.16
 */
@Service
public class VatanSmsServiceImpl implements VatanSmsService {

    public static final String KULNO = "40972";
    public static final String KULAD = "905524646407";
    public static final String KULSIFRE = "07UUkQ64";
    public static final String ORJINATOR = "ASELSIS";
    public static final String TIP = "Turkce";    // or "Normal"
    public static final String ADRES = "http://www.oztekbayi.com/webservis/service.php";


    @Override
    public String replaceMessage(String orjMessage) {

        return orjMessage
                .replace("\n", "|61|")
                .replace("ı", "|67|")
                .replace("İ", "|72|")
                .replace("ö", "|62|")
                .replace("Ö", "|68|")
                .replace("ğ", "|66|")
                .replace("Ğ", "|73|")
                .replace("ü", "|63|")
                .replace("Ü", "|69|")
                .replace("ş", "|65|")
                .replace("Ş", "|71|")
                .replace("ç", "|64|")
                .replace("Ç", "|70|");
    }

    /**
     * @param status             values --> "START" or "FINISH"
     * @param number             number to send
     * @param qrCode             qrCode
     * @param qrCodeUrl           qrCodeUrl
     * @param recipientFirstname  recipient Firstname
     * @param recipientLastname   recipient Lastname
     * @return void.
     * @since 1.0.16
     */
    @Override
    public void sendSms(String number, String qrCode, String qrCodeUrl, String recipientFirstname, String recipientLastname, String status) {

        try {
            String time = "";        //İleri tarih için kullanabilirsiniz. 2014-04-21 10:00:00
            String timeOut = "";    //Sms ömrünü belirtir. 2014-04-21 15:00:00  (ZAMAN AŞIMI)

            String message = "";

            if (status.equals("START")) {
                message = replaceMessage("Sayın ") + replaceMessage(recipientFirstname.toUpperCase(new Locale("tr", "TR"))) + " " + replaceMessage(recipientLastname.toUpperCase(new Locale("tr", "TR")))
                        + "," + "|61|" + replaceMessage("Kargoyu teslim etmek için qr kodunuz: ") + qrCode + " ." + replaceMessage("Qr Kodu Görüntülemek İçin Tıklayınız: ") +
                        qrCodeUrl;
            } else if (status.equals("FINISH")) {
                message = replaceMessage("Sayın ") + replaceMessage(recipientFirstname.toUpperCase(new Locale("tr", "TR"))) + " " + replaceMessage(recipientLastname.toUpperCase(new Locale("tr", "TR")))
                        + "," + "|61|" + replaceMessage("Kargoyu teslim almak için qr kodunuz: ") + qrCode + " ." + replaceMessage("Qr Kodu Görüntülemek İçin Tıklayınız: ") +
                        qrCodeUrl;
            } else {

                throw new SmsException("Sms Exception");
            }

            String TekSmsiBirdenCokNumarayaGonder = "<?xml version='1.0' encoding='utf-8'?>" +
                    "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>" +
                    "<soap:Body>" +
                    "<TekSmsiBirdenCokNumarayaGonder xmlns='" + ADRES + "'>" +
                    "<kullanicino>" + KULNO + "</kullanicino>" +
                    "<kullaniciadi>" + KULAD + "</kullaniciadi>" +
                    "<sifre>" + KULSIFRE + "</sifre>" +
                    "<orjinator>" + ORJINATOR + "</orjinator>" +
                    "<numaralar>" + number + "</numaralar>" +
                    "<mesaj>" + message + "</mesaj>" +
                    "<zaman>" + time + "</zaman>" +
                    "<zamanasimi>" + timeOut + "</zamanasimi>" +
                    "<tip>" + TIP + "</tip>" +
                    "</TekSmsiBirdenCokNumarayaGonder>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            POST(ADRES, TekSmsiBirdenCokNumarayaGonder);

        } catch (SmsException e) {

            throw new SmsException("sms exception");
        } catch (Exception e) {

            throw new SmsException(e.getLocalizedMessage());
        }
    }

    @Override
    public String POST(String _Adres, String _Xml) {
        HttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(_Adres);
            post.setHeader("Content-type", "text/xml; charset=utf-8");
            post.setEntity(new StringEntity(_Xml));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String cevap = EntityUtils.toString(entity);
                cevap = cevap.split("<return xsi:type=\"xsd:string\">")[1];
                return cevap.split("</return>")[0];
            }
            return null;

        } catch (Exception ex) {

            throw new SmsException(ex.getLocalizedMessage());

        } finally {
            client.getConnectionManager().shutdown();
        }
    }


}
