import { Card, Space,Progress,Row,Col,Button,Alert,Modal,Typography,Upload,message,Table,Input,Radio} from 'antd';
import { useState,useRef, useEffect } from 'react' 
import React from 'react';
import CustomTitle from '../../components/CustomTitle';
import SupportBanner from '../../components/SupportBanner';
import { InboxOutlined } from '@ant-design/icons';
import {PaymentControllReceipt} from '../../api/controllers/payment-controller-receipt';
import { useSelector } from 'react-redux';
import { ColumnsType, TableProps } from 'antd/es/table';
import Highlighter from 'react-highlight-words';
import { SearchOutlined } from '@ant-design/icons';
import { getCurrentPaymentRequests } from '../../api/controllers/current-payment-request';
import RequestError from '../../components/RequestError';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';


const { Paragraph } = Typography;


const CurrentPayment= (transport,updatePage) => {
  const { t, i18n } = useTranslation("common");
  /*Cari Bilgileri*/
  const [totalLimitTL, setTotalLimitTL] = useState(0);
  const [paidTL, setPaidTL] = useState(0);
  const [remainingTL, setRemainingTL] = useState(0); 
  const [totalLimitUsd, setTotalLimitUsd] = useState(0);
  const [paidUsd, setPaidUsd] = useState(0);
  const [remainingUsd, setRemainingUsd] = useState(0); 
  const [totalLimitEuro, setTotalLimitEuro] = useState(0);
  const [paidEuro, setPaidEuro] = useState(0);
  const [remainingEuro, setRemainingEuro] = useState(0); 
/*USer Bilgisi*/
  const currentUser = useSelector((state) => state.user);
  const CompanyName= currentUser.companyCommercialTitle;
  /*Modal Ödeme*/
  const [modalVisible, setModalVisible] = useState(false);
  const handlePaymentClick = () => {
    setModalVisible(true);
  };

  const handleModalClose = () => {
    setModalVisible(false);
  };

  const { Dragger } = Upload;
  const props = {
    name: 'file',
    multiple: false,
    beforeUpload: async (file) => {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('advertID', transport.id)
  
      try {
        const response = await PaymentControllReceipt(formData) ;
  
        if (response.status === 200) {
          message.success(`${file.name} dosyası başarıyla yüklendi.`);
          updatePage();
        } else {
          message.error(`Dosya yükleme işlemi başarısız oldu.`);
        }
      } catch (error) {
        message.error(`Dosya yükleme işlemi sırasında bir hata oluştu: ${error.message}`);
      }
  
      return false;
    },
  };

  /*Ödeme Aşaması Kart İçin */

  /*Modal Cari Hesaplar İçin */
  const [modalVisiblePayment,setModalVisiblePayment]=useState(false);

  const handleCardClick = () => {
    setModalVisiblePayment(true);
  };

  const handleCardModalClose = () => {
    setModalVisiblePayment(false);
  };

  /*Modal Cari İşlemlerin Gösterilmesi */
  const onChange = (pagination, filters, sorter, extra) => {

  };

   
 
  const [searchText, setSearchText] = useState('');
  const [searchedColumn, setSearchedColumn] = useState('');
  const searchInput = useRef(null);
  const [currentRequests, setCurrentRequests] = useState([]);
  const [adsLoading, setAdsLoading] = useState(true);
  const [pageable, setPageable] = useState({
    page: 1,
    pageSize: 10,
    totalSize: null,
    sort: "",
})
const [searchParams, setSearchParams] = useSearchParams();
let param = searchParams.get("status");
const [isActive, setIsActive] = useState(param === "passive" ? false : true)
 
  const getAds = async () => {
    setAdsLoading(true)
    try {
        let res = await getCurrentPaymentRequests();
        if (res) {
          setCurrentRequests(res.data);
        }
    } catch (error) {
        RequestError(error);
    }
    setAdsLoading(false)
}
useEffect(() => {
  getAds();
  
  setPageable({ ...pageable, page: 1 })
}, [isActive])

const data = currentRequests.map(val => {
  return { 
          key: "1",
          date: val.dateofShipment,
          startingAddress: val.startingCountry,
          dueAddress:val.dueCountry,
          bid:val.BidPrice,
          insurance:val.insurancePrice,
          abroudordomestic:val.AbroadOrDomestic,
          transportId: val.TransportID
      };
});
console.log(data)
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
          placeholder={`Search ${dataIndex}`}
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
            Search
          </Button>
          <Button
            onClick={() => clearFilters && handleReset(clearFilters)}
            size="small"
            style={{
              width: 90,
            }}
          >
            Reset
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
            Filter
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              close();
            }}
          >
            close
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
  const columns = [
    {
      title: 'Yükleme Noktası',
      dataIndex: 'startingAddress',
      key: 'startingAddress',
      width: '25%',
      ...getColumnSearchProps('startingAddress'),
    },
    {
      title: 'Varış Noktası',
      dataIndex: 'dueAddress',
      key: 'dueAddress',
      width: '25%',
      ...getColumnSearchProps('dueAddress'),
    },
    {
      title: 'Tarih',
      dataIndex: 'date',
      key: 'date',
      ...getColumnSearchProps('date'),
     
    },
    {
      title: 'Teklif',
      dataIndex: 'bid',
      key: 'bid',
      ...getColumnSearchProps('bid'),
     
    },
    {
      title: 'Sigorta',
      dataIndex: 'insurance',
      key: 'insurance',
      ...getColumnSearchProps('insurance'),
     
    },
    
    
  ];
return(
  <div className="layout-content-padding">
  <div style={{ marginBottom: "1rem" }}>
    <CustomTitle>{"Cari Bilgileri"}</CustomTitle>
  </div>
  <Space direction="vertical" style={{ width: '100%' }}>
  <Alert
      message="Ödeme Tarihi :23/06/2023"
      type="warning"
      showIcon
    />
</Space>
  <Row gutter={[16, 16]} justify="center">
    <Col span={8}>
      <Card title="Cari Bilgileri ₺" style={{ width: 300, margin: '0 auto', marginTop: '2rem' }}
      onClick={handleCardClick}
      >
        <div style={{ textAlign: "center" }}>
          <Progress type="dashboard" percent={75} gapDegree={30} />
        </div>
        <br />
        <p>
          <span style={{ display: "block" }}><b>Toplam Limit:</b> {totalLimitTL} ₺</span>
          <span style={{ display: "block" }}><b>Harcanan Tutar:</b> {paidTL} ₺</span>
          <span style={{ display: "block" }}><b>Kalan Limit:</b> {remainingTL} ₺</span>
        </p>
      </Card>
      <Modal
        title="Cari Bilgileri ₺ "
        open={modalVisiblePayment}
        onCancel={handleCardModalClose}
        width="70%"
        footer={null}
      >
        <Table columns={columns} dataSource={data} loading={adsLoading}/>
      </Modal>
    </Col>
    <Col span={8}>
      <Card title="Cari Bilgileri $" style={{ width: 300, margin: '0 auto',marginTop: '2rem' }}>
        <div style={{ textAlign: "center" }}>
          <Progress type="dashboard" percent={75} gapDegree={30} />
        </div>
        <br />
        <p>
          <span style={{ display: "block" }}><b>Toplam Limit:</b> {totalLimitUsd} $</span>
          <span style={{ display: "block" }}><b>Harcanan Tutar:</b> {paidUsd} $</span>
          <span style={{ display: "block" }}><b>Kalan Limit:</b> {remainingUsd} $</span>
        </p>
      </Card>
    </Col>
    <Col span={8}>
      <Card title="Cari Bilgileri €" style={{ width: 300, margin: '0 auto',marginTop: '2rem' }}>
        <div style={{ textAlign: "center" }}>
          <Progress type="dashboard" percent={75} gapDegree={30} />
        </div>
        <br />
        <p>
          <span style={{ display: "block" }}><b>Toplam Limit:</b> {totalLimitEuro} €</span>
          <span style={{ display: "block" }}><b>Harcanan Tutar:</b> {paidEuro} €</span>
          <span style={{ display: "block" }}><b>Kalan Limit:</b> {remainingEuro} €</span>
        </p>
      </Card>
    </Col>
    <Button type="primary" style={{ width: 300, margin: '0 auto', marginTop: '2rem' }} onClick={handlePaymentClick}>
  Ödeme Yap
</Button>

<Modal
  title="Ödeme"
  open={modalVisible}
  onCancel={handleModalClose}
  footer={[
    <Button key="cancel" onClick={handleModalClose}>
      İptal
    </Button>,
    <Button key="pay" type="primary" onClick={handleModalClose}>
      Öde
    </Button>
  ]}
>

</Modal>
  </Row>
  <SupportBanner />
</div>

)
}






export default CurrentPayment