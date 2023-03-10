package com.lucas.bank.ledger.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@DynamoDBDocument
@Getter
@Setter
@AllArgsConstructor
@Builder
public class JournalTransactionPOJO {

    private String journalTransactionId;
    private String operationType;
    private BigDecimal amount;
    private Integer ledgerAccountId;
    private String ledgerAccountName;
    private String accountType;
    private Long transactionDate;
    private Long bookingDate;

    public JournalTransactionPOJO(){ }
}
