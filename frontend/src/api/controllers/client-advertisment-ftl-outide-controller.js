import axios from "axios";

export const createFtlOutsideAdvertisementRequest = (aracForm) => axios.post(`/abroud/add`, aracForm)