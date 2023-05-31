import axios from "axios";

export const getTrailerFloorTypesRequest = () => axios.get(`/trailerFloorType`);

export const createTrailerFloorTypeRequest = (trailerFloorType) => axios.post(`/trailerFloorType`, trailerFloorType);

export const getTrailerFloorTypeByIdRequest = (id) => axios.get(`/trailerFloorType/${id}`);

export const deleteTrailerFloorTypeRequest = (id) => axios.delete(`/trailerFloorType/${id}`);

export const getTrailerFloorTypeActivateRequest = (id) => axios.get(`/trailerFloorType/${id}/activate`);

export const getTrailerFloorTypeHideRequest = (id) => axios.get(`/trailerFloorType/${id}/hide`);