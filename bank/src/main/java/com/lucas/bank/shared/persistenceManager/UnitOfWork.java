package com.lucas.bank.shared.persistenceManager;

import com.lucas.bank.shared.dynamoDb.DynamoDbTransaction;
import com.lucas.bank.shared.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class UnitOfWork {

    private final Logger LOG = LoggerFactory.getLogger(UnitOfWork.class);

    private final UUID transactionId;
    private final LocalDateTime transactionStartDate;
    private final List<Object> transactions;
    private final DynamoDbTransaction dynamoDbTransaction;

    private UnitOfWork() {
        transactionId = UUID.randomUUID();
        transactionStartDate = DateTimeUtil.nowWithTimeZone();
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

        LOG.info("Commit transaction completed, transaction id: {}, start date: {}, current size: {}", this.transactionId, this.transactionStartDate, this.transactions.size());
    }
}
