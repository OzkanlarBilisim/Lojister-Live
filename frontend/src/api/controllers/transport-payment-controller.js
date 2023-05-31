import axios from "axios";

export const getAllPaymentsRequest = () => axios.get(`/transportPayment`);

export const payTheDriverRequest = (form) => axios.post(`/transportPayment`, form);

export const getCompletedPaymentsRequest = () => axios.get(`/transportPayment/paymentCompleted`);

export const getWaitingPaymentsRequest = () => axios.get(`/transportPayment/paymentWaiting`);

export const getPaymentByTransportProcessIdRequest = (transportProcessId) => axios.get(`/transportPayment/transportProcess/${transportProcessId}`);

export const getPaymentDetailByTransportProcessIdRequest = (transportCode) => axios.get(`/transportPayment/transportProcess/detail?transportCode=${transportCode}`);
