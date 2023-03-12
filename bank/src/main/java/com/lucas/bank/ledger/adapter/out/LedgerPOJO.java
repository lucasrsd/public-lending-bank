package com.lucas.bank.ledger.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.lucas.bank.ledger.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.lucas.bank.shared.staticInformation.StaticInformation.SINGLE_TABLE_NAME;

@DynamoDBTable(tableName = SINGLE_TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LedgerPOJO {

    public static final String pkPrefix = "ledger#";
    public static final String skPrefix = "loan#";

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey
    private String sk;

    private String ledgerTransactionEntryId;
    private Long loanId;
    private Long ledgerTransactionId;
    private String ledgerTransactionType;
    private String ledgerTransactionSide;
    private BigDecimal ledgerTransactionAmount;
    private Integer ledgerAccountId;
    private String ledgerAccountName;
    private String ledgerAccountType;
    private Long ledgerDate;
    private Long ledgerBookingDate;

    public LedgerPOJO() {
    }

    public static String buildPk(String ledgerTransactionEntryId) {
        return pkPrefix + ledgerTransactionEntryId;
    }

    public static String buildSk(Long loanId, Long transactionId) {
        return skPrefix + loanId + "#" + transactionId;
    }

    public String getPk() {
        return buildPk(ledgerTransactionEntryId);
    }

    public String getSk() {
        return buildSk(loanId, ledgerTransactionId);
    }
}
