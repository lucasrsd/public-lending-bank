package com.lucas.bank.shared.staticInformation;


import java.math.RoundingMode;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class StaticInformation {
    public static String getAwsRegion(){
        return System.getenv("AWS_REGION");
    }

    public static String getBatchMainRegion(){
        return System.getenv("BATCH_MAIN_REGION");
    }

    public static Long getRegionIdPrefix(){
        var region = getAwsRegion();
        if (region.equals("eu-west-1"))
            return 10000000000000L;
        if (region.equals("eu-central-1"))
            return 40000000000000L;
        if (region.equals("eu-north-1"))
            return 70000000000000L;

        return 0L;
    }

    public static final String TIME_ZONE_ID = "Europe/Amsterdam";
    public static final Integer CALCULATION_PRECISION_SCALE = 10;
    public static final Integer TRANSACTION_PRECISION_SCALE = 2;
    public static final RoundingMode TRANSACTION_ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final Integer MONTH_PERIOD = 30;
    public static final Integer YEAR_TO_MONTH_PERIOD = 12;
    public static final Integer DAY_TO_YEAR_PERIOD = 360;
    public static final String LOAN_STATE_BY_BATCH_BLOCK_GSI_INDEX = "loan-state-by-batch-block-gsi";
    public static final String LEDGER_ENTRIES_BY_LOAN_ID_GSI_INDEX = "ledger-entries-by-loan-gsi";
    public static final String SINGLE_TABLE_NAME = "bank-data";
    public static final String LOCK_TABLE_NAME = "bank-lock";
    public static final String CONFIGURATION_TABLE_NAME = "bank-configuration";
    public static final Integer BATCH_BLOCK_RANGE = 100;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final Integer generateRandomBatchBlock(){
        return ThreadLocalRandom.current().nextInt(1, BATCH_BLOCK_RANGE);
    }

    public static String DATABASE_URL(){
        return System.getenv("DATABASE_URL");
    }

    public static String DATABASE_SECRET_NAME(){
        return System.getenv("DATABASE_SECRET_NAME");
    }
}
