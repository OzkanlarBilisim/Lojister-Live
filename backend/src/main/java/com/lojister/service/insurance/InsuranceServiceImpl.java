package com.lojister.service.insurance;


import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.business.abstracts.ClientService;
import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.business.abstracts.UserService;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.entity.adminpanel.CargoType;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.payment.Insurance;
import com.lojister.repository.payment.InsuranceRepository;
import com.lojister.service.abroudService.AbroudService;
import com.lojister.service.api.TcmbApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InsuranceServiceImpl implements InsuranceService{
    @Autowired
    private TcmbApiService tcmbApiService;
    @Autowired
    private MailNotificationService mailNotificationService;
    @Autowired
    private ClientAdvertisementService clientAdvertisementService;
    @Autowired
    private UserService userService;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private AbroudService abroudService;
    @Autowired
    private ClientService clientService;

    public Insurance insurance(Double goodsPrice, String startCountryCode, String dueCountryCode, String advertCurrencySymbol, String InsuranceType, String InsuranceCurencySymbol){
        Insurance insurance = new Insurance();


        insurance.setInsuranceCurency("?");
        insurance.setInsurancePrice(0.0);
        insurance.setInsurancePriceTl(0.0);
        insurance.setTransactionType("undifined");
        insurance.setRate(0.0);
        insurance.setInsuranceType("UNINSURED");



        if(startCountryCode.equals("TR") || dueCountryCode.equals("TR")){
            if (!InsuranceType.equals("UNINSURED")){
                if(goodsPrice != null) {


                            Double usd = Double.valueOf(tcmbApiService.getUsd());
                            Double euro = Double.valueOf(tcmbApiService.getEuro());
                            Double rate = 1d;


                            Double goodsPriceTl = goodsPrice;
                            if (advertCurrencySymbol.equals("$")) {
                                goodsPriceTl = goodsPrice * usd;
                            }
                            if (advertCurrencySymbol.equals("€")) {
                                goodsPriceTl = goodsPrice * euro;
                            }


                            Double insurancePrice = null;
                            Double comprehensiveprice = null;
                            String transactionType = null;
                            if (startCountryCode.equals(dueCountryCode)) {
                                insurancePrice = goodsPriceTl > 30303 ? goodsPriceTl * 0.001155 : 35;
                                comprehensiveprice = goodsPriceTl > 15217 ? goodsPriceTl * 0.0023 : 35;
                                transactionType = "Domastic";
                            } else {
                                if (dueCountryCode.equals("TR")) {
                                    insurancePrice = goodsPriceTl > 30303 ? goodsPriceTl * 0.001155 : 35;
                                    comprehensiveprice = goodsPriceTl > 15217 ? goodsPriceTl * 0.0023 : 35;
                                    transactionType = "Import";
                                }
                                if (startCountryCode.equals("TR")) {
                                    insurancePrice = goodsPriceTl > 32000 ? goodsPriceTl * 0.0011 : 35;
                                    comprehensiveprice = goodsPriceTl > 15909 ? goodsPriceTl * 0.0022 : 35;
                                    transactionType = "Export";
                                }
                            }


                            insurance.setInsurancePriceTl(InsuranceType.equals("NARROW") ? Math.ceil(insurancePrice) : Math.ceil(comprehensiveprice));

                            if (InsuranceCurencySymbol.equals("$")) {
                                insurancePrice = insurancePrice / usd;
                                comprehensiveprice = comprehensiveprice / usd;
                                rate = usd;
                            }
                            if (InsuranceCurencySymbol.equals("€")) {
                                insurancePrice = insurancePrice / euro;
                                comprehensiveprice = comprehensiveprice / euro;
                                rate = euro;
                            }


                            insurance.setInsuranceCurency(InsuranceCurencySymbol);
                            insurance.setInsurancePrice(InsuranceType.equals("NARROW") ? Math.ceil(insurancePrice) : Math.ceil(comprehensiveprice));
                            insurance.setTransactionType(transactionType);
                            insurance.setRate(rate);
                            insurance.setInsuranceType(InsuranceType);


                }
            }
        }



        return insurance;
    }

    public String sendMail(String transportID, Insurance insurance){
        String name = null;
        String taxNumber = null;
        String address = null;
        String goodPrice = null;
        String startIngDate = null;
        String startIngAddress = null;
        String dueAddress = null;
        String insuranceType = insurance.getInsuranceType().equals("NARROW")? "Dar kapsamlı teminat": "Geniş kapsamlı teminat";
        String referenceNO = null;
        String cargoTypeResult = null;
        String transactionType = null;

        if (!insurance.getInsuranceType().equals("UNINSURED")){
            if (insurance.getTransactionType().equals("Domastic")){
                ClientAdvertisement clientAdvertisement = clientAdvertisementService.findDataById(Long.valueOf(transportID));

                Set<CargoType> cargoTypes = clientAdvertisement.getCargoTypes();

                for (CargoType cargoType : cargoTypes) {
                    cargoTypeResult = String.valueOf(cargoType.getTypeName());
                    break;
                }
                name = clientAdvertisement.getClient().getFirstName() + " "+ clientAdvertisement.getClient().getLastName();
                taxNumber = clientAdvertisement.getClient().getCompany().getTaxNumber();
                address = clientAdvertisement.getClient().getCompany().getAddress().getFullAddress();

                goodPrice = clientAdvertisement.getGoodsPrice()+clientAdvertisement.getCurrencyUnit().getCurrencySymbol();
                startIngDate = clientAdvertisement.getAdStartingDate().toString();
                startIngAddress = clientAdvertisement.getStartingAddress().getFullAddress()+" "+ clientAdvertisement.getStartingAddress().getNeighborhood()+ " / "+clientAdvertisement.getStartingAddress().getDistrict()+" / "+clientAdvertisement.getStartingAddress().getProvince();
                dueAddress = clientAdvertisement.getDueAddress().getFullAddress()+" "+ clientAdvertisement.getDueAddress().getNeighborhood()+ " / "+clientAdvertisement.getDueAddress().getDistrict()+" / "+clientAdvertisement.getDueAddress().getProvince();
                referenceNO = clientAdvertisement.getClientAdvertisementCode();

                transactionType = "Yurt İçi";
            }else {
                AdAbroud adAbroud =  abroudService.IdFind(Integer.valueOf(transportID)).get(0);
                Client client = clientService.findDataById(Long.valueOf(adAbroud.getClient_id()));

                cargoTypeResult = adAbroud.getCargoTypeIdList();
                name = client.getFirstName() + " "+ client.getLastName();
                taxNumber = client.getCompany().getTaxNumber();
                address = client.getCompany().getAddress().getFullAddress();

                goodPrice = adAbroud.getGoodsPrice()+adAbroud.getCurrencyUnitId();
                startIngDate = adAbroud.getStartDate();
                startIngAddress = adAbroud.getStartFullAddress();
                dueAddress = adAbroud.getDueFullAddress();
                referenceNO = adAbroud.getBankID()+transportID;

                if(insurance.getInsuranceType().equals("Import")){
                    transactionType = "İthalat";
                }else {
                    transactionType = "İhracat";
                }
            }

                String message = null;
                if(insurance.getInsuranceCurency().equals("$")){
                    message = "<h3>Bu Poliçe Dolar Olarak Düzenlemesi istenmiştir</h3>";
                }
                if(insurance.getInsuranceCurency().equals("₺")){
                    message = "<h3>Bu Poliçe Türk Lirası Olarak Düzenlemesi istenmiştir</h3>";
                }
                if(insurance.getInsuranceCurency().equals("€")){
                    message = "<h3>Bu Poliçe Euro Olarak Düzenlemesi istenmiştir</h3>";
                }

                message = message + "<table border='1'>" +
                        "  <tr>" +
                        "    <td>SİGORTALI UNVAN </td>" +
                        "    <td>"+name+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>SİGORTALI VERGI NO </td>" +
                        "    <td>"+taxNumber+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>SİGORTALI ADRES </td>" +
                        "    <td>"+address+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>MAL BEDELİ </td>" +
                        "    <td>"+goodPrice+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>Sigorta Bedeli </td>" +
                        "    <td>"+insurance.getInsurancePrice()+insurance.getInsuranceCurency()+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>MAL CİNSİ </td>" +
                        "    <td>"+cargoTypeResult+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>YUKLEME TARİHİ </td>" +
                        "    <td>"+startIngDate+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>CIKIS YERI </td>" +
                        "    <td>"+startIngAddress+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>VARIS YERI </td>" +
                        "    <td>"+dueAddress+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>Kur </td>" +
                        "    <td>1"+insurance.getInsuranceCurency()+" = "+insurance.getRate()+"₺</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>Ticaret Türü </td>" +
                        "    <td>"+transactionType+"</td>"+
                        "  </tr>" +
                        "    <td>Teminat Türü </td>" +
                        "    <td>"+insuranceType+"</td>"+
                        "  </tr>" +
                        "  <tr>" +
                        "    <td>Referance No </td>" +
                        "    <td>"+referenceNO+"</td>"+
                        "  </tr>" +
                        "</table>";

                // String[] emailList = new String[]{"mehmet.ozfiliz@helmetbroker.com.tr","dilek.guven@helmetbroker.com.tr","galip.gokakin@helmetbroker.com.tr"};
                String[] emailList = new String[]{"h.ozer@lojister.com"};
                for (String email: emailList){
                    mailNotificationService.send(email, "Lojister Poliçe", message);
                }
        }

        return "mail Gönderildi";
    }
    public Insurance save(Insurance insurance){
        if(!insurance.getInsuranceType().equals("UNINSURED")){
            return insuranceRepository.save(insurance);
        }
        return null;
    }
}
