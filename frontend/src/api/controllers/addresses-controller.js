import axios from "axios";

export const getDistrictRequest = (provinceName) => axios.get(`/addresses/district?provinceName=${provinceName}`);

export const getNeighborhoodRequest = (districtId) => axios.get(`/addresses/district/${districtId}/neighborhood`);

export const getDistrictByIdRequest = (districtId) => axios.get(`/addresses/district/${districtId}`);

export const createGeocodeFromAddressRequest = (address) => axios.post(`/addresses/geocode`, address);

export const getNeighborhoodByProvinceAndDistrictNameRequest = ({ provinceName, districtName }) => axios.get(`/addresses/neighborhood?provinceName=${provinceName}&districtName=${districtName}`);

export const getNeighborhoodByIdRequest = (neighborhoodId) => axios.get(`/addresses/neighborhood/${neighborhoodId}`);

export const getProvinceRequest = () => axios.get(`/addresses/province`);

export const getProvinceByIdRequest = (provinceId) => axios.get(`/addresses/province/${provinceId}`);

export const getDistrictByProvinceIdRequest = (provinceId) => axios.get(`/addresses/province/${provinceId}/district`);

export const reverseGeocodeRequest = (geocode) => axios.post(`/addresses/reverseGeocode`, geocode);

export const getByWordRequest = (word) => axios.post(`/addresses/word`, word);