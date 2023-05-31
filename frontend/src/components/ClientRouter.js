import React, { Fragment } from "react"
import { useState, useEffect } from "react"
import { Navigate, Outlet } from "react-router-dom"
import SuspenseFallback from "./SuspenseFallback"
import { getMeRequest } from "../api/controllers/account-controller"
import { useDispatch } from "react-redux"
import { setUser } from "../redux/actions/userActions"
import CustomLayout from "./CustomLayout"
import AntNotification from "./AntNotification"
import { useTranslation } from "react-i18next"

const ClientRouter = () => {
    const { t, i18n } = useTranslation("common");

    const dispatch = useDispatch();
    const [AUTH_STATUS, setAUTH_STATUS] = useState("WAITING")

    const checkAuthentication = async () => {
        try {
            let res = await getMeRequest();
            if (res && res.status === 200) {
                dispatch(setUser(res.data));
                setTimeout(() => {
                    setAUTH_STATUS("SUCCESS")
                }, 1000);
            }
        } catch (error) {
            if (error?.response?.status === 401) {
                AntNotification({ type: "error", message: t("İşleminize devam edebilmek için giriş yapın") });
                dispatch(setUser(null));
                setAUTH_STATUS("FAILED")
            }
        }
    }

    useEffect(() => {
        checkAuthentication()
    }, [])

    return (
        <Fragment>
            {
                AUTH_STATUS === "WAITING" ? <SuspenseFallback />
                    : AUTH_STATUS === "FAILED" ? <Navigate to="/login" />
                        : AUTH_STATUS === "SUCCESS" ? <CustomLayout><Outlet /></CustomLayout>
                            : <div>Error</div>
            }
        </Fragment>
    )
}

export default ClientRouter;