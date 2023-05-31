import axios from "axios";

export const createContainerAdvertisementRequest = (containerData) => axios.post(`/clientAdvertisement/container`, containerData)