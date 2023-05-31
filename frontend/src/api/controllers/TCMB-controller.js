import axios from "axios";

export const getDolar = () => axios.get(`/tcmb/dolar`);
export const getEuro = () => axios.get(`/tcmb/euro`);