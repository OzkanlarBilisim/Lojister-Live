import { Spin } from 'antd';
import React from 'react'

const SuspenseFallbackInline = ({ whiteBg }) => {
    return (
        <div style={{ width: "100%", height: "100%", display: "grid", justifyContent: "center", alignContent: "center", backgroundColor: whiteBg ? "rgba(255,255,255,.5)" : "transparent" }}>
            <Spin />
        </div>
    )
}

export default SuspenseFallbackInline;