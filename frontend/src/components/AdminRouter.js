import React, { Fragment } from "react"
import { useState, useEffect } from "react"
import { Navigate, Outlet } from "react-router-dom"
import SuspenseFallback from "./SuspenseFallback"
import { getMeRequest } from "../api/controllers/account-controller"
import { useDispatch } from "react-redux"
import { setUser } from "../redux/actions/userActions"
import AdminLayout from "./AdminLayout"

const AdminRouter = () => {
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
                        : AUTH_STATUS === "SUCCESS" ? <AdminLayout><Outlet /></AdminLayout>
                            : <div>Error</div>
            }
        </Fragment>
    )
}

export default AdminRouter;