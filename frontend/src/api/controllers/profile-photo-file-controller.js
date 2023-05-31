import axios from "axios";

export const getProfilePhotoFileRequest = (id) => axios.get(`/profilePhotoFile/${id}/file`);

export const getProfilePhotoUrlRequest = (id) => axios.get(`/profilePhotoFile/${id}/url`);

export const getProfilePhotoBase64Request = (id) => axios.get(`/profilePhotoFile/user/${id}/base64`);

export const deleteMyProfilePhotoRequest = () => axios.delete(`/profilePhotoFile/my`);

export const getMyProfilePhotoBase64Request = () => axios.get(`/profilePhotoFile/my/base64`);

export const getMyProfilePhotoFileRequest = () => axios.get(`/profilePhotoFile/my/file`);

export const getMyProfilePhotoUrlRequest = () => axios.get(`/profilePhotoFile/my/url`);

export const uploadProfilePhotoRequest = (photo) => axios.post(`/profilePhotoFile/upload`, photo);