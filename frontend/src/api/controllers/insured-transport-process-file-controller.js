import axios from "axios";

export const downloadInsuranceFileByTransportCodeRequest = (transportCode) => axios.get(`/transportProcess/insured/file/download?transportCode=${transportCode}`)
export const reguestDocument = (advertId, wanting) => axios.get(`/DocumentsAbroud/advertId/${advertId}/wanting/${wanting}`)
export const reguestDocumentListClient = (advertId) => axios.get(`/files/fileList/Client/${advertId}`)
export const reguestDocumentListDriver = (advertId) => axios.get(`/files/fileList/Driver/${advertId}`)
export const requestTransportInfo = (advertId) => axios.get(`/shipsmentInfo/advertId/${advertId}`)
export const DriverSendRequest = (data) => axios.post(`/DocumentsAbroud/add`, data)
export const fileUploadAbroud = (formData) => axios.post("/files/", formData, {
  headers: {
    "Content-Type": "multipart/form-data; boundary=<calculated when request is sent>",
  }
  });