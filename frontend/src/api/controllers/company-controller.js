import axios from "axios";

export const getCompaniesRequest = () => axios.get(`/company`);

export const updateCompanyRequest = (company) => axios.put(`/company`, company);

export const getCompanyByIdRequest = (id) => axios.get(`company/${id}`);

export const updateCompanyCommissionRateRequest = (commissionRate) => axios.put(`/company/commissionRate`, commissionRate);

export const getCompanyByCommercialTitleRequest = (commercialTitle) => axios.get(`/company/findByCommercialTitle?commercialTitle=${commercialTitle}`);

export const getMyCompanyRequest = () => axios.get(`/company/myCompany`);

export const getRatingInfoRequest = () => axios.get(`/company/ratingInformation`);