import React from 'react'

export const CustomButton = ({ children, style, icon, type, color, disabled, htmlType, onClick }) => {
    return (
        <button
            onClick={onClick}
            disabled={disabled}
            className={"custom-button " + (color ? color : "primary") + " " + (type ? type : "")}
            style={style}
            type={htmlType ? htmlType : "button"}
        >
            {icon && icon}{children}
        </button>
    )

};