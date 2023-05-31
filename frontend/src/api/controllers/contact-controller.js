import axios from "axios";

export const createContactMailRequest = (form) => axios.post(`/contact/sendMail`, form);