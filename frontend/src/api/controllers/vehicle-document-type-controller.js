import axios from "axios";

export const getAllVehicleDocumentTypeRequest = () => axios.get(`/vehicleDocumentType`);

export const createVehicleDocumentTypeRequest = (form) => axios.post(`vehicleDocumentType`, form);

export const getVehicleDocumentTypeByIdRequest = (id) => axios.get(`/vehicleDocumentType/${id}`);

export const deleteVehicleDocumentTypeRequest = (id) => axios.delete(`/vehicleDocumentType/${id}`);

export const getActivateVehicleDocumentTypeByIdRequest = (id) => axios.get(`/vehicleDocumentType/${id}/activate`);

export const getHideVehicleDocumentTypeByIdRequest = (id) => axios.get(`/vehicleDocumentType/${id}/hide`);
