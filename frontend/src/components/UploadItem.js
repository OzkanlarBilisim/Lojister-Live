import { useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import { useTranslation } from "react-i18next";
import { getDriverDocumentBase64Request } from "../api/controllers/driver-document-file-controller";
import { getVehicleDocumentBase64Request } from "../api/controllers/vehicle-document-file-controller";

function UploadItem({ id, name, filesForSend, setFilesForSend, documentId, type }) {
    const { t, i18n } = useTranslation("common");

    const [files, setFiles] = useState([])
    const [file, setFile] = useState("")

    const { getRootProps, getInputProps } = useDropzone({
        multiple: false,
        accept: {
            'image/*': [],
            'application/pdf' : []
        },

        onDrop: acceptedFiles => {
            acceptedFiles.map(file => Object.assign(file, {
                preview: URL.createObjectURL(file),
                id: id
            }))
            setFiles(acceptedFiles)
            let filesForSend_m = filesForSend
            filesForSend_m[acceptedFiles[0].id] = acceptedFiles[0]
            setFilesForSend(filesForSend_m);
        }
    });

    const thumbs = files.map(file => {
        return (
            <embed
                    key={file.name}
                    src={file.preview}
                    type={file.type}
                    width="100%"
                    height="100px"
                    
                    
                />
        )
    });

    const displayProtectedImage = async () => {
        try {
            let res
            if (type === "driver") {
                res = await getDriverDocumentBase64Request(documentId);
            } else if (type === "vehicle") {
                res = await getVehicleDocumentBase64Request(documentId);
            }
            if (res) {
                setFile(res.data);
            }
        } catch (error) {

        }
    }

    useEffect(() => {
        if (documentId) {
            displayProtectedImage();
        }
    }, [documentId])

    useEffect(() => () => {
        files.forEach(file => URL.revokeObjectURL(file.preview));
    }, [files]);

    return (
        <>
            {
                documentId ?
                    <div className="upload-image-area" style={{ cursor: "not-allowed" }}>
                        <img
                            src={`data:${file.type};base64,${file.data}`}
                        />
                    </div> :
                    <div className="upload-image-area" {...getRootProps()}>
                        <input {...getInputProps()} />
                        {
                            thumbs.length > 0
                                ? thumbs
                                : <p>{t("Yüklemek için tıklayın!")}</p>
                        }
                    </div>
            }
            <p style={{
                textAlign: "center",
                marginTop: ".5rem"
            }}>
                {name}
            </p>
        </>
    );
};

export default UploadItem;