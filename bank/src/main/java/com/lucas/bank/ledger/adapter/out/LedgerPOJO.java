package com.lucas.bank.ledger.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.lucas.bank.shared.staticInformation.StaticInformation.SINGLE_TABLE_NAME;

@DynamoDBTable(tableName = SINGLE_TABLE_NAME)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LedgerPOJO {

    public static final String pkPrefix = "loan#";
    public static final String skPrefix = "ledger-entry#";

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey
    private String sk;

    private String ledgerEntryId;
    private Long loanId;
    private String transactionType;
    private JournalTransactionPOJO credit;
    private JournalTransactionPOJO debit;
    private Long transactionDate;

    public LedgerPOJO() {
    }

    public static LedgerPOJO of(Long loanId, String entryId){
        return LedgerPOJO.builder().loanId(loanId).ledgerEntryId(entryId).build();
    }

    public static String buildPk(Long loanId) {
        return pkPrefix + loanId;
    }

    public static String buildSk(String ledgerEntryId) {
        return skPrefix + ledgerEntryId;
    }

    public String getPk() {
        return buildPk(loanId);
    }

    public String getSk() {
        return buildSk(ledgerEntryId);
    }
}
