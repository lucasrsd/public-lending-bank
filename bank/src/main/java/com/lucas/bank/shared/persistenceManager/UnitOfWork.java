package com.lucas.bank.shared.persistenceManager;

import com.lucas.bank.shared.dynamoDb.DynamoDbTransaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class UnitOfWork {

    private final Logger LOG = LoggerFactory.getLogger(UnitOfWork.class);

    private final UUID transactionId;
    private final Date transactionStartDate;
    private final List<Object> transactions;
    private final DynamoDbTransaction dynamoDbTransaction;

    private UnitOfWork() {
        transactionId = UUID.randomUUID();
        transactionStartDate = new Date();
        transactions = new ArrayList<>();
        dynamoDbTransaction = new DynamoDbTransaction();
    }

    public static UnitOfWork newInstance() {
        return new UnitOfWork();
    }

    public void addTransaction(Object object) {
        transactions.add(object);

        LOG.info("New transaction added, transaction id: {}, start date: {}, current size: {}", this.transactionId, this.transactionStartDate, this.transactions.size());
    }

    public void commit() {
        LOG.info("Starting commit transaction, transaction id: {}, start date: {}, current size: {}", this.transactionId, this.transactionStartDate, this.transactions.size());

        dynamoDbTransaction.commitTransaction(transactions);

        transactions.clear();

        LOG.info("Commit transaction, transaction id: {}, start date: {}, current size: {}", this.transactionId, this.transactionStartDate, this.transactions.size());
    }
}
