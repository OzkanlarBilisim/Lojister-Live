import React, { useState, useEffect } from 'react';
import { Button, Modal } from 'antd';
import axios from 'axios';

const CardModal = () => {
  const [modalVisible, setModalVisible] = useState(false);
  const [cardData, setCardData] = useState([]);
  const [cardData2, setCardData2] = useState([]);

  useEffect(() => {
    fetchCardData();
  }, []);

  const fetchCardData = async () => {
    try {
      const response = await axios.get('http://localhost:8083/api/v2/paymentMethod/cards');
      setCardData(response.data);
    } catch (error) {
      console.error(error);
    }
  };
  

  const handleButtonClick = () => {
    setModalVisible(true);
  };
  const handleButton2Click = () => {
    axios.post('http://localhost:8083/api/v2/paymentMethod/selected-card', {
      "abroudorDomestic":"Domastic",  
      "advertId":"199",
      "paymentId":"Rru8W96cDy9tuJWBpA0VEw=="
    })
      .then(response => {
        
        console.log(response.data);
      })
      .catch(error => {
       
        console.error(error);
      });
  };
  

  const handleModalClose = () => {
    setModalVisible(false);
  };

  return (
    <div>
      <Button type="primary" onClick={handleButtonClick}>
        Modalı Aç
      </Button>
      <Modal
        title="Card Bilgileri"
        visible={modalVisible}
        onCancel={handleModalClose}
        footer={null}
      >
        {cardData.map(card => (
          <div key={card.cardName}>
            <p>Kart Adı: {card.cardName}</p>
            <p>Kart Numarası: {card.maskedCardNumber}</p>
            <p>Kart Id: {card.paymentId}</p>
          </div>
        ))}<Button type='primary' onClick={handleButton2Click}></Button>
      </Modal>
      <Button type='primary' onClick={handleButton2Click}>sasdasd</Button>
    </div>
  );
};

export default CardModal;
