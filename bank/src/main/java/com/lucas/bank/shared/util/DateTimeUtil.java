package com.lucas.bank.shared.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    public static Long to(Date date){
        if (date == null) return null;
        return date.getTime() / 1000;
    }

    public static Date from(Long value){
        if (value == null) return null;
        return new Date(value * 1000);
    }

    public static Date addMonthsAndRetrieveNextBusinessDay(Date date, Integer months){
        var cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        while(cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7){
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
