import axios from "axios";

export const createPartialAdvertisementRequest = (partialData) => axios.post(`/clientAdvertisement/partial`, partialData)