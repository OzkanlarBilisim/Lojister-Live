import axios from "axios";

export const deleteVehicleDocumentRequest = (id) => axios.delete(`/vehicleDocument/${id}`);

export const getVehicleDocumentFileRequest = (id) => axios.get(`/vehicleDocument/${id}/file`);

export const getVehicleDocumentUrlRequest = (id) => axios.get(`/vehicleDocument/${id}/url`);

export const getVehicleDocumentBase64Request = (id) => axios.get(`/vehicleDocument/base64/${id}`);

export const getVehicleDocumentBase64ListRequest = (idList) => axios.post(`/vehicleDocument/base64/list`, idList);

export const getVehicleDocumentInfoRequest = (id) => axios.get(`/vehicleDocument/info/${id}`);

export const getVehicleDocumentInfoListRequest = () => axios.get(`/vehicleDocument/info/list`);

export const getVehicleDocumentInfoListByVehicleIdRequest = (vehicleId) => axios.get(`/vehicleDocument/info/list/${vehicleId}`);

export const deleteVehicleDocumentFilesByIdListRequest = (idList) => axios.delete(`/vehicleDocument/list`, idList);

export const createVehicleDocumentRequest = (documentTypeId, vehicleId, file) => axios.post(`/vehicleDocument/upload?documentTypeId=${documentTypeId}&vehicleId=${vehicleId}`, file, { headers: { 'Content-Type': 'multipart/form-data', } });
