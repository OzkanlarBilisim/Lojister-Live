import React, { useEffect, useState } from 'react'
import { message, Upload } from 'antd';
import ImgCrop from 'antd-img-crop';
import { getMyProfilePhotoBase64Request } from '../api/controllers/profile-photo-file-controller';
import { BASE_URL } from '../api/ApiProvider';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';

const ProfilePhoto = () => {
    const { t, i18n } = useTranslation("common");

    const token = localStorage.getItem("token");
    const [imageUrl, setImageUrl] = useState(null)

    const onPreview = async (file) => {
        let src = file.url;
        if (!src) {
            src = await new Promise((resolve) => {
                const reader = new FileReader();
                reader.readAsDataURL(file.originFileObj);

                reader.onload = () => resolve(reader.result);
            });
        }

        const image = new Image();
        image.src = src;
        const imgWindow = window.open(src);
        imgWindow?.document.write(image.outerHTML);
    };

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

    const uploadButton = (
        <p style={{ textAlign: "center", padding: ".5rem" }}>
            + {t("Profil fotoğrafı ekle")}
        </p>
    );

    useEffect(() => {
        fetchMyProfilePhoto();
    }, [])

    return (
        <div className="profile-photo" >
            <ImgCrop rotate modalTitle={t("Profil fotoğrafı ekle")}>
                <Upload
                    accept="image/*"
                    action={`${BASE_URL}/profilePhotoFile/upload`}
                    encType="multipart/form-data"
                    headers={{
                    Authorization: `Bearer ${token}`,
                    }}
                    showUploadList={false}
                    onChange={(x) => {
                    x.file.status === "uploading" &&
                        message.loading(t("Resim yükleniyor"));
                    x.file.status === "done" &&
                        message.success(t("Resim yüklendi")).then(() => fetchMyProfilePhoto());
                    }}
                    onPreview={onPreview}
                >
                    <div className="profile-photo-content">
                    {imageUrl ? (
                        <img src={`data:${imageUrl.type};base64,${imageUrl.data}`} alt="" />
                    ) : (
                        uploadButton
                    )}
                    </div>
                </Upload>
            </ImgCrop>

        </div>
    )
}

export default ProfilePhoto;