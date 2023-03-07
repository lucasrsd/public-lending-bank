package com.lucas.bank.shared;


public class StaticInformation {
    public static final String AWS_REGION_STRING = "eu-west-1";
    public static final Integer PRECISION_SCALE = 8;
    public static final Integer MONTH_PERIOD = 30;
    public static final Integer YEAR_TO_MONTH_PERIOD = 12;
    public static final Integer DAY_TO_YEAR_PERIOD = 365;
    public static final String LEDGER_ENTRIES_GSI_INDEX = "ledger-entries-gsi";
    public static final String SINGLE_TABLE_NAME = "bank";
    public static final String LOCK_TABLE_NAME = "bank-lock";
}
