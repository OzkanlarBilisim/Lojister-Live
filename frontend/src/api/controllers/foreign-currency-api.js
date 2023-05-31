import axios from "axios";

export const getTodayForeignCurrencyUsd = () => axios.get('/tcmb/dolar');
export const getTodayForeignCurrencyEuro = () => axios.get('/tcmb/euro');
  