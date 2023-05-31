import { Avatar } from 'antd';
import React, { useEffect, useState } from 'react'
import { getMyProfilePhotoBase64Request, getProfilePhotoBase64Request } from '../api/controllers/profile-photo-file-controller';
import RequestError from './RequestError';

const ShowProfilePhoto = ({ profileId }) => {
    const [imageUrl, setImageUrl] = useState(null)

    const fetchMyProfilePhoto = async () => {
        try {
            let res = await getMyProfilePhotoBase64Request();
            if (res) {
                setImageUrl(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchProfilePhotoById = async () => {
        try {
            let res = await getProfilePhotoBase64Request(profileId);
            if (res) {
                setImageUrl(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    useEffect(() => {
        if (profileId) {
            fetchProfilePhotoById();
        } else {
            fetchMyProfilePhoto();
        }
    }, [profileId])

    return (
        imageUrl ?
            <img
                style={{
                    width: 48,
                    height: 48,
                    objectFit: "contain",
                    borderRadius: "50%",
                    border: "1px solid var(--primary)",
                    flexShrink: 0,
                }}
                src={`data:${imageUrl?.type};base64,${imageUrl?.data}`}
                alt=""
            /> :
            <Avatar
                style={{
                    backgroundColor: "var(--blue-light)",
                    border: "1px solid var(--primary)",
                    flexShrink: 0
                }}
                size={48}
                icon={<i style={{ color: "var(--blue)" }} className="bi bi-person"></i>}
            />
    )
}

export default ShowProfilePhoto;