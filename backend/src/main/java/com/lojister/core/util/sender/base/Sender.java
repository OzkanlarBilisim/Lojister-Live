package com.lojister.core.util.sender.base;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface Sender<T> {

    void send(T t) throws MessagingException, UnsupportedEncodingException;

}
