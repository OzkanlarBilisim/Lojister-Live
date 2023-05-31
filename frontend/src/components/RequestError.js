import { Modal, Result } from 'antd';
import { useTranslation } from 'react-i18next';
import AntNotification from './AntNotification';


function RequestError(error) {
    console.log(error);
    if (error?.response?.status === 400) {
        error?.response?.data?.message?.map(v =>
            AntNotification({ type: "error", message: v })
        )
    } else if (error?.response?.status === 403) {
        AntNotification({ type: "error", message: "Bu işlemi yapabilmek için yetkiniz bulunmuyor" });
    } else if (error?.code === "ERR_NETWORK") {
        Modal.error({
            width: 800,
            title: null,
            content: (
                <Result
                    status="500"
                    title={"Hay Aksi!"}
                    subTitle={"Sunucularımızda bulunan bir sorundan dolayı işleminizi gerçekleştiremiyoruz. Lütfen daha sonra tekrar deneyin."}
                />
            ),
            icon: null,
            onOk: () => window.location.reload(),
            okText: "Sayfayı yenile",
        })
    } else {
        AntNotification({ type: "error", message: "Hay aksi!", description: "Bir hata meydana geldi, lütfen daha sonra tekrar deneyin." })
    }
}
export default RequestError;
/* import { Modal, Result } from 'antd';
import { useTranslation } from 'react-i18next';
import AntNotification from './AntNotification';


function RequestError(error) {
    const { t, i18n } = useTranslation("common");
    console.log(error);
    if (error?.response?.status === 400) {
        error?.response?.data?.message?.map(v =>
            AntNotification({ type: "error", message: v })
        )
    } else if (error?.response?.status === 403) {
        AntNotification({ type: "error", message: t("Bu işlemi yapabilmek için yetkiniz bulunmuyor") });
    } else if (error?.code === "ERR_NETWORK") {
        Modal.error({
            width: 800,
            title: null,
            content: (
                <Result
                    status="500"
                    title={t("Hay Aksi!")}
                    subTitle={t("Sunucularımızda bulunan bir sorundan dolayı işleminizi gerçekleştiremiyoruz. Lütfen daha sonra tekrar deneyin.")}
                />
            ),
            icon: null,
            onOk: () => window.location.reload(),
            okText: t("Sayfayı yenile"),
        })
    } else {
        AntNotification({ type: "error", message: t("Hay aksi!"), description: t("Bir hata meydana geldi, lütfen daha sonra tekrar deneyin.") })
    }
}
export default RequestError; */