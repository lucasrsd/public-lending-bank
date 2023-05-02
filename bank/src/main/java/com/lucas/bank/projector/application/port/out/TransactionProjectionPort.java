package com.lucas.bank.projector.application.port.out;

import com.lucas.bank.transaction.adapter.out.TransactionPOJO;

public interface TransactionProjectionPort {
    void project(TransactionPOJO transactionPOJO);
}
