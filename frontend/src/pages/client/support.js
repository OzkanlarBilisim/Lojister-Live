import React from 'react'
import { useTranslation } from 'react-i18next';
import ComingSoon from '../../components/ComingSoon'
import FAQ from '../../components/FAQ'

export default function Support() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Destek")
    return (
        /*   <ComingSoon /> */
        <FAQ />
    )
}
