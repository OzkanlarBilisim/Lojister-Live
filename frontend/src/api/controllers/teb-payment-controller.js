import axios from "axios";

export const tebPaymentOperationRequest = (transportCode, insuranceType) => axios.get(`/payment/3d?transportCode=${transportCode}&insuranceType=${insuranceType}`);

export const tebPaymentFailRequest = (transportCode) => axios.post(`/payment/fail?transportCode=${transportCode}`);

export const tebPaymentSuccessRequest = (transportCode) => axios.post(`/payment/success?transportCode=${transportCode}`);