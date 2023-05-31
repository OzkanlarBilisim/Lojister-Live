import axios from "axios";

export const deleteDriverDocumentRequest = (id) => axios.delete(`/driverDocument/${id}`);

export const getDriverDocumentFileRequest = (id) => axios.get(`/driverDocument/${id}/file`);

export const getDriverDocumentUrlRequest = (id) => axios.get(`/driverDocument/${id}/url`);

export const getDriverDocumentBase64Request = (id) => axios.get(`/driverDocument/base64/${id}`);

export const getDriverDocumentBase64ListRequest = (idList) => axios.post(`/driverDocument/base64/list`, idList);

export const getDriverDocumentInfoRequest = (id) => axios.get(`/driverDocument/info/${id}`);

export const getDriverDocumentInfoListRequest = () => axios.get(`/driverDocument/info/list`);

export const getDriverDocumentInfoListByUserIdRequest = (userId) => axios.get(`/driverDocument/info/list/${userId}`);

export const deleteDriverDocumentFilesByIdListRequest = (idList) => axios.delete(`/driverDocument/list`, idList);

export const createDriverDocumentRequest = (documentTypeId, driverId, file) => axios.post(`/driverDocument/upload?documentTypeId=${documentTypeId}&driverId=${driverId}`, file, { headers: { 'Content-Type': 'multipart/form-data', } });
