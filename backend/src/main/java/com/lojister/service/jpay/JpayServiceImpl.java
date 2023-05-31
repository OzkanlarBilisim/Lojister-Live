package com.lojister.service.jpay;

import com.est.jpay;
import com.lojister.core.exception.JpayResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JpayServiceImpl implements JpayService {


    @Override
    public Boolean checkPayment(jpay jpayInstance, String host, int port, String path) {

        if (jpayInstance.processTransaction(host, port, path) > 0) {

            System.out.println(jpayInstance.getErrMsg() + "errormsj");
            System.out.println(jpayInstance.getProcReturnCode() + "procreturncode");
            System.out.println(jpayInstance.getResponse() + "response");

            return jpayInstance.getProcReturnCode().equals("00") && jpayInstance.getResponse().equals("Approved");

        } else {
            throw new JpayResponseException("Sanal pos hatası.","Virtual pos error.");
        }

    }
}
