import React from 'react'
import PropTypes from 'prop-types';
import { Modal } from 'antd';

export const CustomModal = ({ content, keyboard, visible, title, okText, cancelText, onOk, onCancel, footer, style, wrapClassName, width }) => {
    return (
        <>
            <Modal
                visible={visible}
                onOk={onOk}
                onCancel={onCancel}
                okText={okText}
                cancelText={cancelText}
                footer={footer}
                keyboard={keyboard}
                centered
                title={title}
                style={style}
                wrapClassName={wrapClassName}
                width={width}
            >
                {content}
            </Modal>
        </>
    )

};

/* CustomModal.propTypes = {
    type: PropTypes.oneOf(['outlined']),
    color: PropTypes.oneOf(['primary', 'primary-light', 'secondary', 'secondary-light', 'light']),
};
 */
