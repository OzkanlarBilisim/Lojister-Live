import { notification } from 'antd';

const AntNotification = ({ message, description, type, placement }) => {

    notification[type]({
        message: message,
        description: description,
        placement: placement ? placement : 'bottomLeft',
    });
}

export default AntNotification