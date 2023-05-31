import axios from "axios";

export const halkBankPaymentOperationRequest = (transportCode, insuranceType) => axios.get(`/payment/halkbank/3d?transportCode=${transportCode}&insuranceType=${insuranceType}`);

export const halkBankPaymentOperationRequestAbroud = (transportCode, insuranceType) => axios.get(`/payment/halkbank/abroud/3d?transportCode=${transportCode}&insuranceType=${insuranceType}`);

export const halkBankPaymentFailRequest = (transportCode) => axios.post(`/payment/halkbank/fail?transportCode=${transportCode}`);

export const halkBankPaymentSuccessRequest = (transportCode) => axios.post(`/payment/halkbank/success?transportCode=${transportCode}`);