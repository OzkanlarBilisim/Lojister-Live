import axios from "axios";

export const getAboutUsRequest = () => axios.get(`/aboutUs`);

export const updateAboutUsEngRequest = (about) => axios.put(`/aboutUs/eng`, about);

export const updateAboutUsTrRequest = (about) => axios.put(`/aboutUs/tr`, about);