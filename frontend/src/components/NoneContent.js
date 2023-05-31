import React from 'react'

const NoneContent = ({ style, vertical, title, description }) => {
    return (
        <div
            style={{
                width: "100%",
                display: "flex",
                flexDirection: vertical ? "column" : "row",
                justifyContent: vertical ? "flex-start" : "center",
                alignItems: "center",
                padding: "2rem",
                flexWrap: "wrap",
                flexFlow: "inherit",
                gap: "1.5rem",
                ...style
            }}>
            <img
                style={{
                    width: "70%",
                    height: "100%",
                    maxWidth: "300px",
                }} src='../../assets/img/nothing-here.svg' alt="" />
            <div>
                <p
                    style={{
                        color: "var(--primary)",
                        fontWeight: 500,
                        fontSize: "1.25rem",
                        textAlign: vertical ? "center" : "left",
                    }}
                >{title}</p>
                <p
                    style={{
                        textAlign: vertical ? "center" : "left",
                    }}
                >{description}</p>
            </div>
        </div >
    )
}

export default NoneContent;