import axios from "axios";

export const getAllFaq = () => axios.get(`/frequentlyAskedQuestions`);

export const createFaqRequest = (faq) => axios.post(`/frequentlyAskedQuestions`, faq);

export const getFaqByIdRequest = (id) => axios.get(`/frequentlyAskedQuestions/${id}`);

export const updateFaqByIdRequest = (faq) => axios.put(`/frequentlyAskedQuestions/${faq.id}`, faq);

export const deleteFaqByIdRequest = (id) => axios.delete(`/frequentlyAskedQuestions/${id}`);