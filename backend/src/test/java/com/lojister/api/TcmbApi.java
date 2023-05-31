package com.lojister.api;

import com.lojister.service.api.TcmbApiService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestOperations;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/tcmb")
public class TcmbApi {
    @Autowired
    private TcmbApiService tcmbApiService;


    @GetMapping("/dolar")
    public String getDolarValue(){
       return tcmbApiService.getUsd();
    }

    @GetMapping("/euro")
    public String getEuroValue(){
       return tcmbApiService.getEuro();
    }

    @GetMapping("/NewDolar")
    public String run() throws IOException, JSONException {

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(formatter);

        String apiUrl = "https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.EUR.S.YTL&startDate=" + formattedDate + "&endDate=" + formattedDate + "&type=json&key=TUU5YNpdUD";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(request);
        String content = EntityUtils.toString(response.getEntity());


        JSONObject json = new JSONObject(content);
        JSONArray items = json.getJSONArray("items");
        JSONObject firstItem = items.getJSONObject(0);
        String tp_dk_eur_s_ytl_value = firstItem.getString("TP_DK_EUR_S_YTL");

        return tp_dk_eur_s_ytl_value;
    }
}
