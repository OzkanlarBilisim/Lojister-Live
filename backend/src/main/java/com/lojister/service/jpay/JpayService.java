package com.lojister.service.jpay;

import com.est.jpay;

public interface JpayService {

    Boolean checkPayment(jpay jpayInstance,String host,int port,String path);

}
