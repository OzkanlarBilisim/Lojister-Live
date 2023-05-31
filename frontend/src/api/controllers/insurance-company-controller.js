import axios from "axios";

export const createInsuranceCompanyRequest = (form) => axios.post(`/insuranceCompany`, form);