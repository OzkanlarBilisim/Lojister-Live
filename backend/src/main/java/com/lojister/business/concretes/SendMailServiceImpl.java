package com.lojister.business.concretes;

import com.lojister.model.dto.NewAdvertisementMailNotificationDto;
import com.lojister.model.dto.SendMailToInsuranceCompanyForNewPaymentDto;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.business.abstracts.SendMailService;
import com.lojister.core.util.sender.AfterPaymentToInsuranceMailSender;
import com.lojister.core.util.sender.NewAdvertisementAdminMailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    private final NewAdvertisementAdminMailSender newAdvertisementAdminMailSender;
    private final AfterPaymentToInsuranceMailSender afterPaymentToInsuranceMailSender;


    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Async
    public void sendMailToAdminForNewAdvertisement(ClientAdvertisement clientAdvertisement) {

        try {
            NewAdvertisementMailNotificationDto newAdvertisementMailNotificationDto = NewAdvertisementMailNotificationDto.builder()
                    .firstName(clientAdvertisement.getClient().getFirstName())
                    .lastName(clientAdvertisement.getClient().getLastName())
                    .startProvince(clientAdvertisement.getStartingAddress().getProvince())
                    .startDistrict(clientAdvertisement.getStartingAddress().getDistrict())
                    .startNeighborhood(clientAdvertisement.getStartingAddress().getNeighborhood())
                    .dueProvince(clientAdvertisement.getDueAddress().getProvince())
                    .dueDistrict(clientAdvertisement.getDueAddress().getDistrict())
                    .dueNeighborhood(clientAdvertisement.getDueAddress().getNeighborhood())
                    .advertisementDate(clientAdvertisement.getCreatedDateTime())
                    .build();

            newAdvertisementAdminMailSender.send(newAdvertisementMailNotificationDto);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("İlan oluşturuldu ama admine mail gönderme işlemi başarısız oldu.");
        }

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Async
    public void sendMailAfterPaymentToInsuranceCompany(ClientTransportProcess clientTransportProcess) {
        try {
            afterPaymentToInsuranceMailSender.send(SendMailToInsuranceCompanyForNewPaymentDto.fromClientTransportProcess(clientTransportProcess));
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Ödeme yapıldı ama sigorta şirketine yollarken hata oluştu.");
        }
    }


}
