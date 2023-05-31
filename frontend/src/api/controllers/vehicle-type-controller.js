import axios from "axios";

export const getVehicleTypesRequest = () => axios.get(`/vehicleType`);

export const createVehicleTypeRequest = (vehicleType) => axios.post(`/vehicleType`, vehicleType);

export const getVehicleTypeByIdRequest = (id) => axios.get(`/vehicleType/${id}`);

export const deleteVehicleTypeRequest = (id) => axios.delete(`/vehicleType/${id}`);

export const getVehicleTypeActivateRequest = (id) => axios.get(`/vehicleType/${id}/activate`);

export const getVehicleTypeHideRequest = (id) => axios.get(`/vehicleType/${id}/hide`);