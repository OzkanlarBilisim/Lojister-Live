package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.enums.TransportProcessType;
import com.lojister.core.exception.WrongTransportProcessType;
import com.lojister.model.entity.TransportProcess;
import com.lojister.repository.transport.TransportProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AdvertisementBidLogic {

    @Autowired
    private TransportProcessRepository transportProcessRepository;

    @Value("${lojister.transport.code}")
    private String clientTransportProcessStartString;


    private static long count = 0;

    //Hergün 00:00'da count sıfırlanacak.
    @Scheduled(cron = "0 0 * * *")
    public void run() {

        try {
            count = 0L;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int codeSplitSize(String transportCode) {

        char[] codeSplit = transportCode.toCharArray();

        for (int i = 0; i < 6; i++) {

            if (codeSplit[i] != '0') {
                return i;
            }
        }
        return 0;
    }


    public Long checkTransportCode(Long count) {

        LocalDate checkDate = LocalDate.now();
        String dateStr = checkDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Optional<TransportProcess> transportProcess = transportProcessRepository.findFirstByTransportCodeContainsOrderByIdDesc(dateStr);

        if (transportProcess.isPresent()) {

            String lastTransportCode = transportProcess.get().getTransportCode();

            lastTransportCode = lastTransportCode.substring(11);

            int charSize = codeSplitSize(lastTransportCode);

            String lastSize;

            lastSize = lastTransportCode.substring(charSize);

            return Long.valueOf(lastSize);

        } else {
            return count;
        }
    }


    public String createTransportCode(LocalDate bidDate, TransportProcessType transportProcessType) {

        String startString;

        if (transportProcessType == TransportProcessType.CLIENT_TRANSPORT_PROCESS) {
            startString = clientTransportProcessStartString;

        } else if (transportProcessType == TransportProcessType.DRIVER_TRANSPORT_PROCESS) {
            startString = "DTP";
        } else {

            //Todo güzel bir mesaj yolla.
            throw new WrongTransportProcessType(Translator.toLocale("lojister.advertisementBidLogic.WrongTransportProcessType"));

        }

        if (count == 0) {
            count = checkTransportCode(count);
        }


        String dateStr = bidDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        count += 1;

        StringBuilder transportCode = new StringBuilder();
        transportCode.append(startString);
        transportCode.append(dateStr);

        int zeroLength = 6;

        String countLength = String.valueOf(count);
        zeroLength = zeroLength - countLength.length();


        for (int i = 1; i <= zeroLength; i++) {

            transportCode.append("0");
        }

        transportCode.append(count);

        return transportCode.toString();

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double calculateVAT(double value, int places) {
        return round(value * 1.18D, places);
    }


}

