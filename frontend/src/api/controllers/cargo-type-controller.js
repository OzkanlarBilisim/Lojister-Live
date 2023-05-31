import axios from "axios";

export const getCargoTypesRequest = () => axios.get(`/cargoType`);

export const createCargoTypeRequest = (cargoType) => axios.post(`/cargoType`, cargoType);

export const getCargoTypeByIdRequest = (id) => axios.get(`/cargoType/${id}`);

export const deleteCargoTypeRequest = (id) => axios.delete(`/cargoType/${id}`);

export const getCargoTypeActivateRequest = (id) => axios.get(`/cargoType/${id}/activate`);

export const getCargoTypeHideRequest = (id) => axios.get(`/cargoType/${id}/hide`);