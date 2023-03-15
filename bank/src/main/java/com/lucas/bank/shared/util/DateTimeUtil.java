package com.lucas.bank.shared.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {
    public static Long to(LocalDateTime date){
        if (date == null) return null;
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime from(Long value){
        if (value == null) return null;
        return Instant.ofEpochMilli(value)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime nowWithTimeZone(){
        return LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime addMonthsAndRetrieveNextBusinessDay(LocalDateTime date, Integer months){

        date = date.plusMonths(months);
        while(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            date = date.plusDays(1L);

        return date;
    }

    public static LocalDateTime convertToMidnight(LocalDateTime dateToConvert) {
        return dateToConvert
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .truncatedTo(ChronoUnit.DAYS);
    }

    public static Boolean isSameDate(LocalDateTime date1, LocalDateTime date2){
        return ChronoUnit.DAYS.between(convertToMidnight(date1), convertToMidnight(date2)) == 0;
    }

    public static Long daysDifference(LocalDateTime date1, LocalDateTime date2){
        return ChronoUnit.DAYS.between(convertToMidnight(date1), convertToMidnight(date2));
    }
}
