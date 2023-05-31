package com.lojister.core.validator.formatter;

import org.springframework.stereotype.Component;

@Component
public class PhoneFormatter implements Formatter<String> {

    @Override
    public String format(String input) {
        input.replaceAll("\\s", "");
        if(!input.contains("~")){
            String areaCode=input.substring(0,3);
            if(areaCode.equals("+90")){
             return   areaCode+"~"+input.substring(3);
            }
        }
        return input;
    }

    public static String staticFormat(String input)  {
        return input.replaceAll("\\s", "");
    }
}
