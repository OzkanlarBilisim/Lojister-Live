package com.lojister.service.vatansms;

/**
 * @author Fatih Mayuk
 * @version %I%, %G%
 * @since 1.0.16
 */
public interface VatanSmsService {

    String replaceMessage(String orjMessage);

    void sendSms(String number, String qrCode, String qrCodeUrl, String recipientFirstname, String recipientLastname, String status);

    String POST(String _Adres, String _Xml);

}