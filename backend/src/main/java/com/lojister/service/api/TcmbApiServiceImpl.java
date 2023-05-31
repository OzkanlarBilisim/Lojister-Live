package com.lojister.service.api;

import com.lojister.model.entity.TcmbData;
import com.lojister.repository.api.TcmbApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TcmbApiServiceImpl implements TcmbApiService{
    @Autowired
    private TcmbApiRepository tcmbApiRepository;



    LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String formattedDate = date.format(formatter);
    public String getUsdApi (Boolean dateOk, int i){
        try {
            if (!dateOk){
                date = date.minusDays(i);
            }

            URL url = new URL("https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.USD.S.YTL&startDate="+formattedDate+"&endDate="+formattedDate+"&type=json&key=TUU5YNpdUD");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonString = response.toString();
            int startIndex = jsonString.indexOf("TP_DK_USD_S_YTL") + 18;
            int endIndex = jsonString.indexOf("\"", startIndex);
            String tp_dk_usd_s_ytl = jsonString.substring(startIndex, endIndex);

            return tp_dk_usd_s_ytl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEuroApi(Boolean dateOk, int i){
        try {
            if (!dateOk){
                date = date.minusDays(i);
            }

            URL url = new URL("https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.EUR.S.YTL&startDate="+formattedDate+"&endDate="+formattedDate+"&type=json&key=TUU5YNpdUD");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonString = response.toString();
            int startIndex = jsonString.indexOf("TP_DK_EUR_S_YTL") + 18;
            int endIndex = jsonString.indexOf("\"", startIndex);
            String tp_dk_usd_s_ytl = jsonString.substring(startIndex, endIndex);

            return tp_dk_usd_s_ytl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEuroApi(){
        String euro = getEuroApi(true, 0);
        if (euro.equals("ull,")){
            String euro2 = "ull,";
            int i = 1;
            while (euro2.equals("ull,")){
                euro2 = getEuroApi(false, i);
                i++;
            }
            saveEuroData(euro2);
            return euro2;
        }else {
            saveEuroData(euro);
            return euro;
        }
    }
    public String getUsdApi(){
        String usd = getUsdApi(true, 0);
        if (usd.equals("ull,")){
            String usd2 = "ull,";
            int i = 1;
            while (usd2.equals("ull,")){
                usd2 = getUsdApi(false, i);
                i++;
            }
            saveUsdData(usd2);
            return usd2;
        }else {
            saveUsdData(usd);
            return usd;
        }
    }
    public void saveUsdData(String x){
        TcmbData tcmbData = null;
        try {
            Optional<TcmbData> optionalTcmbData = tcmbApiRepository.findById(1L);
            tcmbData = optionalTcmbData.orElseGet(TcmbData::new);
        } catch (NoSuchElementException e) {
            tcmbData = new TcmbData();
        }


        tcmbData.setId(1L);
        tcmbData.setUsd(x);
        tcmbData.setAddDateUsd(formattedDate);

        tcmbApiRepository.save(tcmbData);
    }
    public void saveEuroData(String x){
        TcmbData tcmbData = null;
        try {
            Optional<TcmbData> optionalTcmbData = tcmbApiRepository.findById(1L);
            tcmbData = optionalTcmbData.orElseGet(TcmbData::new);
        } catch (NoSuchElementException e) {
            tcmbData = new TcmbData();
        }

        tcmbData.setId(1L);
        tcmbData.setEuro(x);
        tcmbData.setAddDateEuro(formattedDate);

        tcmbApiRepository.save(tcmbData);
    }
    public String getUsd(){
        String usd = null;
        try {
            Optional<TcmbData> optionalTcmbData = tcmbApiRepository.findById(1L);
            TcmbData tcmbData = optionalTcmbData.orElseGet(TcmbData::new);
            if (tcmbData.getAddDateUsd() != null){
                if (tcmbData.getAddDateUsd().equals(formattedDate)){
                    usd = tcmbData.getUsd();
                }else {
                    usd = getUsdApi();
                }
            }else {
                usd = getUsdApi();
            }

        } catch (NoSuchElementException e) {
            usd = getUsdApi();
        }
        return usd;
    }
    public String getEuro(){
        String euro = null;
        try {
            Optional<TcmbData> optionalTcmbData = tcmbApiRepository.findById(1L);
            TcmbData tcmbData = optionalTcmbData.orElseGet(TcmbData::new);
            if (tcmbData.getAddDateEuro() != null){
                if (tcmbData.getAddDateEuro().equals(formattedDate)){
                    euro = tcmbData.getEuro();
                }else {
                    euro = getEuroApi();
                }
            }else {
                euro = getEuroApi();
            }

        } catch (NoSuchElementException e) {
            euro = getEuroApi();
        }
        return euro;
    }
}

