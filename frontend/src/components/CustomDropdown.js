import React, { useRef, useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { Button, Divider, Input, Select, Space, } from 'antd';

let index=0;
const CustomDropdown = ({ options, value, onChange, onAddItem, placeholder }) => {
    const inputRef = useRef(null);
    const [name, setName] = useState('');
  
    const onNameChange = (event) => {
      setName(event.target.value);
    };
  
    const addItem = (e) => {
      e.preventDefault();
      onAddItem(name || `New item ${index++}`);
      setName('');
      setTimeout(() => {
        inputRef.current?.focus();
      }, 0);
    };
  
    return (
      <Select
        style={{
          width: 300
        }}
        value={value}
        onChange={onChange}
        mode="multiple"
        placeholder={placeholder}
        dropdownRender={menu => (
          <>
            {menu}
            <Divider
              style={{
                margin: '8px 0'
              }}
            />
            <Space
              style={{
                padding: '0 8px 4px'
              }}
            >
              <Input
                placeholder="Belge Adı"
                ref={inputRef}
                value={name}
                onChange={onNameChange}
              />
              <Button type="text" icon={<PlusOutlined />} onClick={addItem}>
                Belge İste
              </Button>
            </Space>
          </>
        )}
      >
        {options.map(option => (
          <Select.Option key={option.value} value={option.value}>
            {option.label}
          </Select.Option>
        ))}
      </Select>
    );
  };

  export default CustomDropdown