import { useEffect, useState } from "react";
import { getDolar,getEuro } from "../api/controllers/TCMB-controller";
import RequestError from './RequestError';


export const Insurance = (goodsPrice, startCountryCode, dueCountryCode, advertCurrencySymbol) => {
    const [usd, setUsd] = useState(1);
    const [euro, setEuro] = useState(1);



    const getTodayUsd = async () =>{
        try{
          let res = await getDolar();
            if(res){setUsd(res.data);}
        }catch(Error){
          RequestError(Error);
        }
    }
    const getTodayEuro = async () =>{
        try{
          let res = await getEuro();
            if(res){setEuro(res.data);}
        }catch(Error){
          RequestError(Error);
        }
    }

    let goodsPriceTl = goodsPrice;
    if(advertCurrencySymbol === "$"){
        goodsPriceTl = goodsPrice * usd;
    }
    if(advertCurrencySymbol === "€"){
        goodsPriceTl = goodsPrice * euro;
    }




    let insurancePrice          = 0;
    let comprehensiveprice      = 0;
    if(dueCountryCode === "TR" || startCountryCode === "TR"){
        if(startCountryCode === dueCountryCode){
            insurancePrice      = goodsPriceTl > 30303? goodsPriceTl * 0.001155: 35;
            comprehensiveprice  = goodsPriceTl > 15217? goodsPriceTl * 0.0023: 35;
        }else{
            if(dueCountryCode === "TR"){
                insurancePrice      = goodsPriceTl > 30303? goodsPriceTl * 0.001155: 35;
                comprehensiveprice  = goodsPriceTl > 15217? goodsPriceTl * 0.0023: 35;

                insurancePrice      = insurancePrice / usd;
                comprehensiveprice  = comprehensiveprice / usd;
            }
            if(startCountryCode === "TR"){
                insurancePrice      = goodsPriceTl > 32000? goodsPriceTl * 0.0011: 35;
                comprehensiveprice  = goodsPriceTl > 15909? goodsPriceTl * 0.0022: 35;

                insurancePrice      = insurancePrice / usd;
                comprehensiveprice  = comprehensiveprice / usd;
            }
        }
    }
    insurancePrice          = Math.ceil(insurancePrice);
    comprehensiveprice      = Math.ceil(comprehensiveprice);

    useEffect(()=>{
        getTodayUsd();
        getTodayEuro();
    },[]);

    const returnx = {
        insurancePrice:         insurancePrice,
        comprehensiveprice:     comprehensiveprice,
    }
    return returnx;
}