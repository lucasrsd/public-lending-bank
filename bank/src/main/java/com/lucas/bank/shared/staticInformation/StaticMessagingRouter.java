package com.lucas.bank.shared.staticInformation;

import java.net.URI;

public class StaticMessagingRouter {
    public static final String ROUTER_KEY_NAME = "router";
    public static final String ROUTE_DAILY_BATCH = "daily-batch-job";
    public static final String ROUTE_LOAN_EXTRACTION = "loan-extraction";
    public static final String ROUTE_LOAN_ACCRUAL = "loan-accrual";
    public static final String ROUTE_BANK_SQL_PROJECTOR = "bank-slq-projector-queue";

    public static URI QUEUE_URL_DAILY_BATCH(){
        return URI.create(System.getenv("QUEUE_ARN_LOAN_DAILY_BATCH_JOB"));
    }

    public static URI QUEUE_URL_LOAN_EXTRACTION(){
        return URI.create(System.getenv("QUEUE_ARN_LOAN_ACCRUAL_EXTRACTION"));
    }

    public static URI QUEUE_URL_LOAN_ACCRUAL(){
        return URI.create(System.getenv("QUEUE_ARN_LOAN_ACCRUAL"));
    }

    public static URI QUEUE_URL_BANK_SQL_PROJECTOR(){
        return URI.create(System.getenv("QUEUE_ARN_BANK_SQL_PROJECTOR"));
    }
}
