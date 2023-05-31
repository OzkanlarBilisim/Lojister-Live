import axios from "axios";

export const getTrailerTypesRequest = () => axios.get(`/trailerType`);

export const createTrailerTypeRequest = (trailerType) => axios.post(`/trailerType`, trailerType);

export const getTrailerTypeByIdRequest = (id) => axios.get(`/trailerType/${id}`);

export const deleteTrailerTypeRequest = (id) => axios.delete(`/trailerType/${id}`);

export const getTrailerTypeActivateRequest = (id) => axios.get(`/trailerType/${id}/activate`);

export const getTrailerTypeHideRequest = (id) => axios.get(`/trailerType/${id}/hide`);