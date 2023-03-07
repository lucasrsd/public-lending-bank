package com.lucas.bank.shared.transactionManager;

import com.lucas.bank.shared.dynamoDb.DynamoDbTransaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

// ToDo (Important) - Remove all news()

@RequiredArgsConstructor
@Component
public class PersistenceTransactionManager {

    private final Logger LOG = LoggerFactory.getLogger(PersistenceTransactionManager.class);

    private final UUID transactionId;
    private final Date transactionStartDate;
    private final List<Object> transactions;
    private final DynamoDbTransaction dynamoDbTransaction;

    private PersistenceTransactionManager() {
        transactionId = UUID.randomUUID();
        transactionStartDate = new Date();
        transactions = new ArrayList<>();
        dynamoDbTransaction = new DynamoDbTransaction();
    }

    public static PersistenceTransactionManager newPersistenceTransaction() {
        return new PersistenceTransactionManager();
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
