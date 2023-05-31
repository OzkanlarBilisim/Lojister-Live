import React from 'react'

const CustomTitle = ({ children, small, style }) => {
    return (
        <h4 style={{ color: "var(--dark)", fontWeight: 500, fontSize: small ? "14px" : "18px", marginBottom: "1rem", ...style }}><i style={{ color: "var(--primary)", fontSize: small ? "12px" : "16px" }} className="bi bi-chevron-double-right"></i> {children}</h4>
    )
}

export default CustomTitle;