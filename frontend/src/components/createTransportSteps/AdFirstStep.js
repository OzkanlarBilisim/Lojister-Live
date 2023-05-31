import { Col, Radio, Row, Button, Table,Input, Space } from 'antd';
import React, { useState,useRef } from 'react'
import { useTranslation } from 'react-i18next';
import { CustomButton } from '../CustomButton';
import CustomTitle from '../CustomTitle';
import { CustomModal } from '../CustomModal';
import {getAllAdvertisementYurtdisiClientList, getAllAdvertisementYurtdisiDetay } from '../../api/controllers/client-advertisiment-controller';
import { useSelector } from 'react-redux';
import Highlighter from 'react-highlight-words';
import RequestError from '../../components/RequestError';
import AdvertiseAgain from '../../components/AdvertiseAgain';
import { SearchOutlined } from '@ant-design/icons';


//FIRST STEP
const AdFirstStep = ({ adInfoForm, setAdInfoForm, handleClickNextStep, containerForm, setContainerForm, aracForm, containeroutsideForm, aracpartForm, containeroutsidepartForm }) => {
    const { t, i18n } = useTranslation("common");/* {t("")} */
    const [showModel, setShowModel] = useState(false);
    const [showModel2, setShowModel2] = useState(false);
    const [transports, setTransports] = useState([]);
    const [advertDetail, setAdvertDetail] = useState([]);
    const [adsLoading, setAdsLoading] = useState(false);
    const currentUser = useSelector((state) => state.user);
    
    const searchInput = useRef(null);
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');

    const unShowModel = () =>{
        setShowModel(false);
    }
    const unShowModel2 = () =>{
        setShowModel2(false);
    }
    const getAds = async () => {
        setShowModel(true);
        setAdsLoading(true);
        try {
            let res = await getAllAdvertisementYurtdisiClientList(currentUser?.id);
            if (res) {
                setTransports(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
        setAdsLoading(false)
    }  
    const detail = async (id) => {
        setShowModel2(true);
        try {
            let res = await getAllAdvertisementYurtdisiDetay(id);
            if (res) {
                setAdvertDetail(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
      };
      const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText('');
      };

    const getColumnSearchProps = (dataIndex) => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters, close }) => (
          <div
            style={{
              padding: 8,
            }}
            onKeyDown={(e) => e.stopPropagation()}
          >
            <Input
              ref={searchInput}
              placeholder={`Adresler Arasında Ara`}
              value={selectedKeys[0]}
              onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
              onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
              style={{
                marginBottom: 8,
                display: 'block',
              }}
            />
            <Space>
              <Button
                type="primary"
                onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                icon={<SearchOutlined />}
                size="small"
                style={{
                  width: 90,
                }}
              >
                Ara
              </Button>
              <Button
                onClick={() => clearFilters && handleReset(clearFilters)}
                size="small"
                style={{
                  width: 90,
                }}
              >
                Temizle
              </Button>
              <Button
                type="link"
                size="small"
                onClick={() => {
                  confirm({
                    closeDropdown: false,
                  });
                  setSearchText(selectedKeys[0]);
                  setSearchedColumn(dataIndex);
                }}
              >
                Filtere
              </Button>
              <Button
                type="link"
                size="small"
                onClick={() => {
                  close();
                }}
              >
                Kapat
              </Button>
            </Space>
          </div>
        ),
        filterIcon: (filtered) => (
          <SearchOutlined
            style={{
              color: filtered ? '#1890ff' : undefined,
            }}
          />
        ),
        onFilter: (value, record) =>
          record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownOpenChange: (visible) => {
          if (visible) {
            setTimeout(() => searchInput.current?.select(), 100);
          }
        },
        render: (text) =>
          searchedColumn === dataIndex ? (
            <Highlighter
              highlightStyle={{
                backgroundColor: '#ffc069',
                padding: 0,
              }}
              searchWords={[searchText]}
              autoEscape
              textToHighlight={text ? text.toString() : ''}
            />
          ) : (
            text
          ),
      });
      const pagination = {
        pageSize: 5,
      };
      
    const colulistPreviousPostingsmnsHeader = [
        {
            title: 'Taşıma Tipi',
            dataIndex: 'transportType',
            filters: [
              {
                text: 'FTL',
                value: 'FTL',
                children: [
                  {
                    text: 'Kara Yolu',
                    value: 'FTL / Kara Yolu',
                  },
                  {
                    text: 'Hava Yolu',
                    value: 'FTL / Hava Yolu',
                  },
                  {
                    text: 'Deniz Yolu',
                    value: 'FTL / Deniz Yolu',
                  },
                ],
              },
              {
                text: 'PARSİYEL',
                value: 'PARSİYEL',
                children: [
                  {
                      text: 'Kara Yolu',
                      value: 'PARSİYEL / Kara Yolu',
                    },
                    {
                      text: 'Hava Yolu',
                      value: 'PARSİYEL / Hava Yolu',
                    },
                    {
                      text: 'Deniz Yolu',
                      value: 'PARSİYEL / Deniz Yolu',
                    },
                ],
              },
            ],
            // specify the condition of filtering result
            // here is that finding the name started with `value`
            onFilter: (value, record) => record.transportType.indexOf(value) === 0,
          },
          {
              title: 'Yükleme Adresi',
              dataIndex: 'loadingZone',
              key: '4',
              ...getColumnSearchProps('loadingZone'),
          },
  
          {
              title: 'Teslimat Adresi',
              dataIndex: 'destinationZone',
              key: 'address',
              ...getColumnSearchProps('destinationZone'),
              sortDirections: ['descend', 'ascend'],
            },
  
          {
              title: 'Detaylı İncele',
              dataIndex: 'button',
              key: 'button',
            },
    ];

      const colulistPreviousPostingsmns = transports.map(val => {
        return { 
                key: val.id,
                transportType: val.clientAdvertisementType+" / "+val.vehicleOrContainer,
                loadingZone: val.startFullAddress,
                destinationZone: val.dueFullAddress,
                button: <Button onClick={() => detail(val.id)}>{t("Detaylı İncele")}</Button>
            };
      });

    const changeTransportMethod = (e) =>{
      switch(e.target.value){
        case "Araç":
          adInfoForm.transportMethod = "Araç";
          setAdInfoForm({ ...adInfoForm, transportRota: "Kara Yolu"});
          break;
        case "Deniz Yolu":
          adInfoForm.transportMethod = "Konteyner";
          setAdInfoForm({ ...adInfoForm, transportRota: "Deniz Yolu"});

          if(adInfoForm.transportType === "FTL/OUTSIDE"){
            containeroutsideForm.vehicleOrContainer = "Deniz Yolu";
          }
          if(adInfoForm.transportType === "PARTIAL/OUTSIDE"){
            containeroutsidepartForm.vehicleOrContainer = "Deniz Yolu";
          }
          break;
        case "Hava Yolu":
          adInfoForm.transportMethod = "Konteyner";
          setAdInfoForm({ ...adInfoForm, transportRota: "Hava Yolu"});

          if(adInfoForm.transportType === "FTL/OUTSIDE"){
            containeroutsideForm.vehicleOrContainer = "Hava Yolu";
          }
          if(adInfoForm.transportType === "PARTIAL/OUTSIDE"){
            containeroutsidepartForm.vehicleOrContainer = "Hava Yolu";
          }
          break;
      }
    }

    return (
        <>
                <div className="create-ad-wrapper">
                    <div style={{width: "250px"}}>
                        <Button type="primary" size="large" onClick={() => getAds()}>{t("Aynı ilanı ver")}</Button>
                    </div>
                    <div className="create-ad-step1-item">
                        <CustomTitle small>{t("Taşıma Bölgesi Seçin")}</CustomTitle>
                        <Radio.Group className="create-ad-radio-group" value={adInfoForm.transportArea} onChange={(e) => {
                            adInfoForm.transportType = null
                            adInfoForm.tradeType = null
                            adInfoForm.transportMethod = null
                            adInfoForm.transportArea = e.target.value
                            setAdInfoForm({ ...adInfoForm })
                        }}>
                            <Row gutter={[24, 24]}>
                                <Col xs={12} sm={12} md={8} lg={6}>
                                    <Radio.Button value="INSIDE">
                                        <div className="create-ad-radio">
                                            <img src={`/assets/img/mapTurkey${adInfoForm.transportArea === "INSIDE" ? "Selected" : ""}.svg`} alt='' />
                                            <p>{t("YURT İÇİ")}</p>
                                        </div>
                                    </Radio.Button>
                                </Col>
                                <Col xs={12} sm={12} md={8} lg={6}>
                                    <Radio.Button value="OUTSIDE" >
                                        <div className="create-ad-radio">
                                            <img src={`/assets/img/mapWorld${adInfoForm.transportArea === "OUTSIDE" ? "Selected" : ""}.svg`} alt='' />
                                            <p>{t("YURT DIŞI")}</p>
                                        </div>
                                    </Radio.Button>
                                </Col>
                            </Row>
                        </Radio.Group>
                    </div>

                    {
                        adInfoForm.transportArea === "INSIDE" &&
                        <div className="create-ad-step1-item">
                            <CustomTitle small>{t("Taşıma Tipi Seçin")}</CustomTitle>
                            <Radio.Group className="create-ad-radio-group" value={adInfoForm.transportType} onChange={(e) => setAdInfoForm({ ...adInfoForm, transportType: e.target.value })}>
                                <Row gutter={[24, 24]}>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="FTL">
                                            <div className="create-ad-radio">
                                                <img src={`/assets/img/types/${adInfoForm.transportType === "FTL" ? "active" : ""}FTL.svg`} alt='' />
                                                <p>{t("FTL")} <span style={{ opacity: .5 }}>{t("TAM YÜK")}</span></p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="PARTIAL">
                                            <div className="create-ad-radio">
                                                <img src={`/assets/img/types/${adInfoForm.transportType === "PARTIAL" ? "active" : ""}PARTIAL.svg`} alt='' />
                                                <p>{t("PARSİYEL")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="CONTAINER" disabled={adInfoForm.transportArea === 'OUTSIDE'}>
                                            <div className="create-ad-radio">
                                                <img src={`/assets/img/types/${adInfoForm.transportType === "CONTAINER" ? "active" : ""}CONTAINER.svg`} alt='' />
                                                <p>{t("KONTEYNER")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                </Row>
                            </Radio.Group>
                        </div>
                    }



                    {
                        adInfoForm.transportType === "CONTAINER" &&
                        <div className="create-ad-step1-item">
                            <CustomTitle small>{t("İşlem Tipi Seçin")}</CustomTitle>
                            <Radio.Group className="create-ad-radio-group" value={containerForm.tradeType} onChange={(e) => setContainerForm({ ...containerForm, tradeType: e.target.value })}>
                                <Row gutter={[24, 24]}>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="IMPORT">
                                            <div className="create-ad-radio">
                                                <p>{t("İTHALAT")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="EXPORT">
                                            <div className="create-ad-radio">
                                                <p>{t("İHRACAT")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                </Row>
                            </Radio.Group>
                        </div>
                    }
                    {
                        adInfoForm.transportArea === "OUTSIDE" &&
                        <div className="create-ad-step1-item">
                            <CustomTitle small>{t("Taşıma Tipi Seçin")}</CustomTitle>
                            <Radio.Group className="create-ad-radio-group" value={adInfoForm.transportType} onChange={(e) => setAdInfoForm({ ...adInfoForm, transportType: e.target.value })}>
                                <Row gutter={[24, 24]}>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="FTL/OUTSIDE">
                                            <div className="create-ad-radio">
                                                <img src={`/assets/img/types/${adInfoForm.transportType === "FTL/OUTSIDE" ? "active" : ""}FTL.svg`} alt='' />
                                                <p>{t("FTL")} <span style={{ opacity: .5 }}>{t("TAM YÜK")}</span></p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="PARTIAL/OUTSIDE">
                                            <div className="create-ad-radio">
                                                <img src={`/assets/img/types/${adInfoForm.transportType === "PARTIAL/OUTSIDE" ? "active" : ""}PARTIAL.svg`} alt='' />
                                                <p>{t("PARSİYEL")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                </Row>
                            </Radio.Group>
                        </div>
                    }
                    {
                        adInfoForm.transportType === "FTL/OUTSIDE" &&
                        <div className="create-ad-step1-item">
                            <CustomTitle small>{t("Taşıma Şeklini Seçin")}</CustomTitle>
                            <Radio.Group className="create-ad-radio-group" value={adInfoForm.transportRota} onChange={(e) => changeTransportMethod(e) }>
                            <Row gutter={[24, 24]}>
                                <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="Araç">
                                            <div className="create-ad-radio">
                                            <img src={`/assets/img/types/${adInfoForm.transportRota === "Kara Yolu" ? "active" : ""}PARTIAL.svg`} alt='' />
                                                <p>{t("Araç")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="Deniz Yolu">
                                            <div className="create-ad-radio">
                                            <img src={`/assets/img/types/${adInfoForm.transportRota === "Deniz Yolu" ? "active" : ""}SHIP.svg`} alt='' />
                                                <p>{t("Deniz Yolu")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="Hava Yolu">
                                            <div className="create-ad-radio">
                                            <img src={`/assets/img/types/${adInfoForm.transportRota === "Hava Yolu" ? "active" : ""}PLANE.svg`} alt='' />
                                                <p>{t("Hava Yolu")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                </Row>
                            </Radio.Group>
                        </div>
                    }
                    {
                        adInfoForm.transportType === "PARTIAL/OUTSIDE" &&
                        <div className="create-ad-step1-item">
                            <CustomTitle small>{t("Taşıma Şeklini Seçin")}</CustomTitle>
                            <Radio.Group className="create-ad-radio-group" value={adInfoForm.transportRota} onChange={(e) => changeTransportMethod(e) }>
                                <Row gutter={[24, 24]}>
                                <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="Araç">
                                            <div className="create-ad-radio">
                                            <img src={`/assets/img/types/${adInfoForm.transportRota === "Kara Yolu" ? "active" : ""}PARTIAL.svg`} alt='' />
                                                <p>{t("Araç")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="Deniz Yolu">
                                            <div className="create-ad-radio">
                                            <img src={`/assets/img/types/${adInfoForm.transportRota === "Deniz Yolu" ? "active" : ""}SHIP.svg`} alt='' />
                                                <p>{t("Deniz Yolu")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                    <Col xs={12} sm={12} md={8} lg={6}>
                                        <Radio.Button value="Hava Yolu">
                                            <div className="create-ad-radio">
                                            <img src={`/assets/img/types/${adInfoForm.transportRota === "Hava Yolu" ? "active" : ""}PLANE.svg`} alt='' />
                                                <p>{t("Hava Yolu")}</p>
                                            </div>
                                        </Radio.Button>
                                    </Col>
                                </Row>
                            </Radio.Group>
                        </div>
                    }
                    <CustomButton
                        onClick={() => handleClickNextStep()}
                        color="primary"
                        disabled={!adInfoForm.transportArea || !adInfoForm.transportType || (adInfoForm.transportType === "CONTAINER" && !containerForm.tradeType)}
                        style={{ alignSelf: "flex-end" }}
                    >
                        {t("Devam Et")}
                    </CustomButton>
                </div>
                <CustomModal
                    title="Aynı ilanı tekrar ver."
                    visible={showModel}
                    onCancel={unShowModel}
                    width="70%"
                    content={
                        <Table dataSource={colulistPreviousPostingsmns} columns={colulistPreviousPostingsmnsHeader} pagination={pagination}/>
                    }
                />
                <AdvertiseAgain 
                  showModel2={showModel2} 
                  adInfoForm={adInfoForm} 
                  unShowModel2={unShowModel2} 
                  unShowModel={unShowModel} 
                  advertDetail={advertDetail}  
                  handleClickNextStep={handleClickNextStep}

                  aracForm={aracForm} 
                  containeroutsideForm={containeroutsideForm} 
                  aracpartForm={aracpartForm} 
                  containeroutsidepartForm={containeroutsidepartForm}
                />
        </>
    );
};

export default AdFirstStep;