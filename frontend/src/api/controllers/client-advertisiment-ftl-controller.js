import axios from "axios";

export const createFtlAdvertisementRequest = (ftlForm) => axios.post(`/clientAdvertisement/ftl`, ftlForm)