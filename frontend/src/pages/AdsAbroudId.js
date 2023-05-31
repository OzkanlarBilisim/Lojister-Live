import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';
import { useNavigate } from 'react-router-dom';
import RequestError from '../components/RequestError';
import React, { useEffect, useState } from 'react';
import TransportDetailAbroad from '../components/TransportDetailAbroad';



const AdsAbroadId = () => {
    const navigate = useNavigate();
    const handleBackButtonClick = () => {
        navigate("/")
        
      };
return (
    <>
    <PublicNavbar />
    <TransportDetailAbroad setShowDetail={handleBackButtonClick} />
    <PublicFooter />
    </>
)
}
export default AdsAbroadId