import axios from "axios";

export const getPackagingTypesRequest = () => axios.get(`/packagingType`);

export const createPackagingTypeRequest = (packagingType) => axios.post(`/packagingType`, packagingType);

export const getPackagingTypeByIdRequest = (id) => axios.get(`/packagingType/${id}`);

export const deletePackagingTypeRequest = (id) => axios.delete(`/packagingType/${id}`);

export const getPackagingTypeActivateRequest = (id) => axios.get(`/packagingType/${id}/activate`);

export const getPackagingTypeHideRequest = (id) => axios.get(`/packagingType/${id}/hide`);