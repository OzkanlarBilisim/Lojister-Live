import axios from "axios";

export const createBankInformationRequest = (data) => axios.post(`/bankInformation`, data);

export const getBankInformationRequest = (id) => axios.get(`/bankInformation/${id}`);

export const updateBankInformationRequest = (id) => axios.put(`/bankInformation/${id}`);

export const deleteBankInformationRequest = (id) => axios.delete(`/bankInformation/${id}`);