import { Spin } from 'antd';
import React from 'react'

const SuspenseFallback = () => {
    return (
        <div style={{
            position: 'sticky',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            width: "100vw",
            height: "100vh",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            backgroundColor: "#FFFFFF",
            zIndex: 9999
        }}>
            <Spin size="large" />
            {/*             <object height="175px" width="175px" type="image/svg+xml" data={loading} aria-labelledby="Loading"></object>
 */}            {/*   <img
                src={"/assets/logo.svg"}
                height="300px"
                width="300px"
                alt="Logo"
            />
 */}
        </div>
    )
}

export default SuspenseFallback;