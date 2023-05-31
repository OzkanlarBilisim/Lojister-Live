import axios from "axios";

export const createRatingRequest = (rating) => axios.post(`/ratingClientTransportProcess`, rating);

export const createRatingRequestAbroud = (rating) => axios.post(`/RaitingAbroud/add`, rating);

export const advertStatusStepController = (transport_ID) => axios.post(`/abroud/advertStatusStep/${transport_ID}`);

export const getByCompanyIdAndRatingList = (rating) => axios.get(`/RaitingAbroud/driver/${rating.companyId}`);

export const getRatingScoresByCompanyId = (companyId) => axios.get(`ratingClientTransportProcess/count/company/${companyId}`);