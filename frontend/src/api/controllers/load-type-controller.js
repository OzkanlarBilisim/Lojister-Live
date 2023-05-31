import axios from "axios";

export const getLoadTypesRequest = () => axios.get(`/loadType`);

export const createLoadTypeRequest = (loadType) => axios.post(`/loadType`, loadType);

export const getLoadTypeByIdRequest = (id) => axios.get(`/loadType/${id}`);

export const deleteLoadTypeRequest = (id) => axios.delete(`/loadType/${id}`);

export const getLoadTypeActivateRequest = (id) => axios.get(`/loadType/${id}/activate`);

export const getLoadTypeHideRequest = (id) => axios.get(`/loadType/${id}/hide`);