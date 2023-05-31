import axios from "axios";

export const getAllDriverDocumentTypeRequest = () => axios.get(`/driverDocumentType`);

export const createDriverDocumentTypeRequest = (form) => axios.post(`driverDocumentType`, form);

export const getDriverDocumentTypeByIdRequest = (id) => axios.get(`/driverDocumentType/${id}`);

export const deleteDriverDocumentTypeRequest = (id) => axios.delete(`/driverDocumentType/${id}`);

export const getActivateDriverDocumentTypeByIdRequest = (id) => axios.get(`/driverDocumentType/${id}/activate`);

export const getHideDriverDocumentTypeByIdRequest = (id) => axios.get(`/driverDocumentType/${id}/hide`);
