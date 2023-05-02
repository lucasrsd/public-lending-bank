package com.lucas.bank.shared.mySql;

import com.lucas.bank.shared.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MySqlExtensions {

    private static final Logger log = LoggerFactory.getLogger(MySqlExtensions.class);

    public static PreparedStatement fillParameters(PreparedStatement statement, List<Object> params) throws SQLException {

        var counter = 1;
        for(Object param : params){

            if (param == null){
                log.info("Parsing field index: {}, null value: {}", counter, param);
                statement.setNull(counter++, Types.NULL);
            }
            else if (param instanceof String){
                var truncatedString = param.toString();
                if (param.toString().length() > 255){
                    log.info("Truncating string param, original: {}", param.toString());
                    truncatedString = param.toString().substring(0, 254);
                }
                log.info("Parsing field index: {}, string value: {}", counter, truncatedString);
                statement.setString(counter++, truncatedString);
            }
            else if (param instanceof BigDecimal){
                log.info("Parsing field index: {}, bigdecimal value: {}", counter, param);
                statement.setBigDecimal(counter++, new BigDecimal(param.toString()));
            }
            else if (param instanceof Long){
                log.info("Parsing field index: {}, long value: {}", counter, param);
                statement.setLong(counter++, ((Long) param).longValue());
            }
            else if (param instanceof Integer){
                log.info("Parsing field index: {}, int value: {}", counter, param);
                statement.setInt(counter++, ((Integer) param).intValue());
            }
            else if (param instanceof LocalDate){
                log.info("Parsing field index: {}, local date value: {}", counter, param);
                statement.setTimestamp(counter++,  DateTimeUtil.toTimestamp(DateTimeUtil.toLocalDate(param)));
            }
            else if (param instanceof LocalDateTime){
                log.info("Parsing field index: {}, date value: {}", counter, param);
                statement.setTimestamp(counter++,  DateTimeUtil.toTimestamp(DateTimeUtil.toLocalDateTime(param)));
            }
        }

        return statement;
    }
}
