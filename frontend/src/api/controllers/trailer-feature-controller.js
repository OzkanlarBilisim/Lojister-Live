import axios from "axios";

export const getTrailerFeaturesRequest = () => axios.get(`/trailerFeature`);

export const createTrailerFeatureRequest = (trailerFeature) => axios.post(`/trailerFeature`, trailerFeature);

export const getTrailerFeatureByIdRequest = (id) => axios.get(`/trailerFeature/${id}`);

export const deleteTrailerFeatureRequest = (id) => axios.delete(`/trailerFeature/${id}`);

export const getTrailerFeatureActivateRequest = (id) => axios.get(`/trailerFeature/${id}/activate`);

export const getTrailerFeatureHideRequest = (id) => axios.get(`/trailerFeature/${id}/hide`);