import axios from "axios";

export const getCurrencyUnitsRequest = () => axios.get(`/currencyUnit`);

export const createCurrencyUnitRequest = (currencyUnit) => axios.post(`/currencyUnit`, currencyUnit);

export const getCurrencyUnitByIdRequest = (id) => axios.get(`/currencyUnit/${id}`);

export const deleteCurrencyUnitRequest = (id) => axios.delete(`/currencyUnit/${id}`);

export const getCurrencyUnitActivateRequest = (id) => axios.get(`/currencyUnit/${id}/activate`);

export const getCurrencyUnitHideRequest = (id) => axios.get(`/currencyUnit/${id}/hide`);