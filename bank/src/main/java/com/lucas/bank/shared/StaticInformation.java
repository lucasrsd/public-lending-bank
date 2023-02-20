package com.lucas.bank.shared;

import com.lucas.bank.ledger.domain.LedgerAccount;
import software.amazon.awssdk.regions.Region;

public class StaticInformation {
    public static final Region AWS_REGION = Region.EU_WEST_1;
    public static final Integer PRECISION_SCALE = 8;
    public static final Integer MONTH_PERIOD = 30;
    public static final Integer YEAR_TO_MONTH_PERIOD = 12;
    public static final Integer DAY_TO_YEAR_PERIOD = 365;
    public final static String LOAN_STATE_INDEX = "loan-state-gsi";
    public final static Integer BATCH_BLOCK_SIZE = 100;
}
