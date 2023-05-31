import React from 'react'
import { useTranslation } from 'react-i18next';
import ComingSoon from '../../components/ComingSoon'

export default function Reports() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Raporlarım")

    return (
        <ComingSoon />
    )
}
