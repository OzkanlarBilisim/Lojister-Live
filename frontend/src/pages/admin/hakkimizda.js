import React, { useEffect, useState } from 'react'
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { Button, Col, Divider, Row } from 'antd';
import { getAboutUsRequest, updateAboutUsEngRequest, updateAboutUsTrRequest } from '../../api/controllers/about-us-controller';
import RequestError from '../../components/RequestError';
import AntNotification from '../../components/AntNotification';

const AdminHakkimizda = () => {
    const [aboutUs, setAboutUs] = useState({})

    const fetchAboutUs = async () => {
        try {
            let res = await getAboutUsRequest();
            if (res) {
                setAboutUs(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const handleSubmit = async () => {
        try {
            let en = await updateAboutUsEngRequest(aboutUs);
            let tr = await updateAboutUsTrRequest(aboutUs);
            if (en && tr) {
                fetchAboutUs();
                AntNotification({ type: "success", message: "Değişiklikler kaydedildi" })
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        fetchAboutUs();
    }, [])

    return (
        <>
            <h2>Hakkımızda</h2>
            <Divider />
            <Row gutter={[24, 24]}>
                <Col xs={24} lg={12}>
                    <p style={{ marginBottom: ".5rem" }}><b>Türkçe</b></p>
                    <CKEditor
                        editor={ClassicEditor}
                        data={aboutUs?.tr_explanation}
                        onBlur={(event, editor) => {
                            const data = editor.getData();
                            setAboutUs({ ...aboutUs, tr_explanation: data })
                        }}
                    />
                </Col>
                <Col xs={24} lg={12}>
                    <p style={{ marginBottom: ".5rem" }}><b>İngilizce</b></p>
                    <CKEditor
                        editor={ClassicEditor}
                        data={aboutUs?.eng_explanation}
                        onBlur={(event, editor) => {
                            const data = editor.getData();
                            setAboutUs({ ...aboutUs, eng_explanation: data })
                        }}
                    />
                </Col>
            </Row>

            <div style={{ marginTop: "1rem", display: "flex", justifyContent: "flex-end" }}>
                <Button onClick={() => handleSubmit()} type="primary">Kaydet</Button>
            </div>
            <br />
        </>
    )
}

export default AdminHakkimizda;