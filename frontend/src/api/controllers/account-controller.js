import axios from "axios";

export const updateFirebaseTokenRequest = (token) => axios.put(`/account/firebaseToken`, token);

export const loginRequest = (data) => axios.post(`/account/login`, data);

export const logoutRequest = () => axios.get(`/account/logout`);

export const clientRegisterRequest = (data) => axios.post(`/account/register/client`, data);

export const clientRegisterWebRequest = (data) => axios.post(`/account/register/client/web`, data);

export const driverRegisterRequest = (data) => axios.post(`/account/register/driver`, data);

export const driverRegisterWebRequest = (data) => axios.post(`/account/register/driver/web`, data);

export const resetPasswordRequest = (data) => axios.post(`/account/resetPassword`, data);

export const resetPasswordMailRequest = (data) => axios.post(`/account/resetPasswordMail`, data);

export const getMeRequest = () => axios.get(`/account/user/me`);

export const verifyUserRequest = () => axios.get(`/account/user/verify`);

export const phoneVerificationRequest = (phone) => axios.get(`/account/phone/verification?phone=${phone}`);

export const emailVerificationRequest = (email) => axios.get(`/account/email/verification?email=${email}`);

export const changeLanguageRequest = (lang) => axios.put(`/account/changeLanguage?language=${lang}`);

