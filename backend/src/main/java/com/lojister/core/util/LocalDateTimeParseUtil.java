package com.lojister.core.util;

import com.lojister.core.exception.LocalDateTimeFormatException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class LocalDateTimeParseUtil {


    public LocalDate getLocalDate(String localDate) {

        if(StringUtils.isBlank(localDate)){
            throw new LocalDateTimeFormatException("Yanlış Tarih Formatı Yolladınız.");
        }



        return LocalDate.parse(localDate, DateTimeFormatter.ISO_LOCAL_DATE);

    }
    public static LocalDateTime localDateTimeConverter(String localDateTime) {

        if(StringUtils.isBlank(localDateTime)){
            throw new LocalDateTimeFormatException("Yanlış Tarih Formatı Yolladınız.");
        }

        return LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }

    public LocalTime getLocalTime(String localTime) {

        if (StringUtils.isBlank(localTime)){
            throw new LocalDateTimeFormatException("Yanlış Zaman Formatı Yolladınız.");
        }
        return LocalTime.parse(localTime, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static LocalDate localDateConverter(String localDate) {

        if(StringUtils.isBlank(localDate)){
            throw new LocalDateTimeFormatException("Yanlış Tarih Formatı Yolladınız.");
        }

        return LocalDate.parse(localDate, DateTimeFormatter.ISO_LOCAL_DATE);

    }

    public static LocalTime localTimeConverter(String localTime) {

        if (StringUtils.isBlank(localTime)){
            throw new LocalDateTimeFormatException("Yanlış Zaman Formatı Yolladınız.");
        }
        return LocalTime.parse(localTime, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
