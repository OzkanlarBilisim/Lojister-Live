import axios from "axios";

export const paymentSuccessAbroud = (advertID) => axios.post(`/payment/halkbank/abroud/success?transportCode=${advertID}`);
export const AdvertPayController = (data) => axios.post(`/paymentMethod/3d`, data);
export const CardInfos = () => axios.get('/paymentMethod/cards');
export const sendSelectedCard = (selectedCard) => axios.post('/paymentMethod/selected-card', selectedCard);

