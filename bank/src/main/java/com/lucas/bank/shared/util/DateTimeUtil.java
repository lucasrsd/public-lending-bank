package com.lucas.bank.shared.util;

import com.lucas.bank.shared.staticInformation.StaticInformation;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    public static Long to(LocalDateTime date) {
        if (date == null) return null;
        return date.atZone(ZoneId.of(StaticInformation.TIME_ZONE_ID)).toInstant().toEpochMilli();
    }

    public static LocalDateTime from(Long value) {
        if (value == null) return null;
        return Instant.ofEpochMilli(value)
                .atZone(ZoneId.of(StaticInformation.TIME_ZONE_ID))
                .toLocalDateTime();
    }

    public static LocalDateTime nowWithTimeZone() {
        return LocalDateTime.now()
                .atZone(ZoneId.of(StaticInformation.TIME_ZONE_ID))
                .toLocalDateTime();
    }

    public static LocalDateTime addMonthsAndRetrieveNextBusinessDay(LocalDateTime date, Integer months) {

        date = date.plusMonths(months);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            date = date.plusDays(1L);

        return date;
    }

    public static LocalDateTime convertToMidnight(LocalDateTime dateToConvert) {
        return dateToConvert
                .atZone(ZoneId.of(StaticInformation.TIME_ZONE_ID))
                .toLocalDateTime()
                .truncatedTo(ChronoUnit.DAYS);
    }

    public static Boolean isSameDate(LocalDateTime date1, LocalDateTime date2) {
        var daysDifference = ChronoUnit.DAYS.between(convertToMidnight(date1), convertToMidnight(date2));

        return daysDifference == 0;
    }

    public static Long daysDifference(LocalDateTime date1, LocalDateTime date2) {
        return ChronoUnit.DAYS.between(convertToMidnight(date1), convertToMidnight(date2));
    }

    public static Timestamp toTimestamp(LocalDateTime date) {
        return Timestamp.valueOf(date);
    }

    public static Timestamp toTimestamp(LocalDate date) {
        return Timestamp.valueOf(date.atStartOfDay());
    }

    public static LocalDateTime toLocalDateTime(Object dateTimeObject) {
        if (dateTimeObject instanceof LocalDateTime) {
            return (LocalDateTime) dateTimeObject;
        }
        return null;
    }

    public static LocalDate toLocalDate(Object dateTimeObject) {
        if (dateTimeObject instanceof LocalDate) {
            return (LocalDate) dateTimeObject;
        }
        return null;
    }
}
