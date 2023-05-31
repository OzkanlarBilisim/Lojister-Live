import { Alert, Tooltip } from 'antd';
import moment from 'moment';
import * as React from 'react';

const TransportCard = ({ data, warning, onClick, isActive }) => {
    return (
        <div onClick={onClick} className={isActive ? "transport-card active" : "transport-card"}>
            <div className="transport-card-header">
                <div className="transport-card-title">
                    <p className="transport-card-transportCode">#{data?.clientAdvertisementCode}</p>
                    <p className="transport-card-cargoType">{
                        data?.cargoTypes.length > 1 ?
                            data?.cargoTypes.map(val => val.typeName + ", ") :
                            data?.cargoTypes[0]?.typeName
                    }</p>
                </div>
                <img className="transport-card-clientAdvertisementType" src={`/assets/img/types/${isActive ? "active" : ""}${data?.clientAdvertisementType}.svg`} alt="" />
            </div>
            <div className="transport-card-content">
                <div className="transport-card-date">
                    <Tooltip title={moment(data?.adStartingDate).format('LL')}>
                        <p className="transport-card-dateValue">{moment(data?.adStartingDate).format('Do MMMM')}</p>
                    </Tooltip>
                    <Tooltip title={moment(data?.adDueDate).format('LL')}>
                        <p className="transport-card-dateValue">{moment(data?.adDueDate).format('Do MMMM')}</p>
                    </Tooltip>
                </div>
                <div className="transport-card-divider">
                    <div className="card-divider-from" />
                    <div className="card-divider-line" />
                    <div className="card-divider-to" />
                </div>
                <div className="transport-card-address">
                    <p className="transport-card-addressValue">{data?.startingAddress.province + ", " + data?.startingAddress.district}</p>
                    <p className="transport-card-addressValue">{data?.dueAddress.province + ", " + data?.dueAddress.district}</p>
                </div>
            </div>
            {
                warning &&
                <Alert
                    banner
                    message={warning}
                />
            }
        </div>
    );
};
export default TransportCard;