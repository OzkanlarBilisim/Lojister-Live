import { notification } from 'antd';

const AntNotificationWarning = ({ message, description, type, placement }) => {

    notification[type]({
        message: message,
        description: description,
        placement: placement ? placement : 'bottomLeft',
    });
}

export default AntNotificationWarning;