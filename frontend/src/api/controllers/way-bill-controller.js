import axios from "axios";

export const getWaybillByIdRequest = (id) => axios.get(`/waybill/${id}`);

export const getChangeDeniedRequest = (transportCode) => axios.get(`/waybill/changeDeniedStatus`, transportCode);

export const createWaybillStatusRequest = (waybillStatus) => axios.post(`/waybill/setStatus`, waybillStatus);

export const createWaybillUploadRequest = (transportProcessId, file) => axios.post(`/waybill/upload?transportProcessId=${transportProcessId}`, file);

export const getWaitingWaybillRequest = () => axios.get(`/waybill/waiting`);
