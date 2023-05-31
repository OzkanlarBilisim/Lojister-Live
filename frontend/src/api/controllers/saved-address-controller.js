import axios from "axios";

export const getMyAddressesRequest = () => axios.get(`/savedAddress/myAddresses`);

export const createSavedAddressesRequest = (data) => axios.post(`/savedAddress`, data);

export const getSavedAddressByIdRequest = (id) => axios.get(`/savedAddress/${id}`);

export const updateDefaultAddressRequest = (id) => axios.put(`/savedAddress/${id}/defaultAddress`);

export const updateSavedAddressRequest = (data) => axios.put(`/savedAddress/${data.id}`, data);

export const deleteSavedAddressByIdRequest = (id) => axios.delete(`/savedAddress/${id}`);

export const disableDefaultAddressRequest = () => axios.put(`/savedAddress/defaultAddress/disable`);