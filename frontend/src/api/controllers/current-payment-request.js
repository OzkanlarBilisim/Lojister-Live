import axios from "axios";

export const getCurrentPaymentRequests = () => axios.get(`/currentPayment/requests`);