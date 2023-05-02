package com.lucas.bank.ledger.adapter.out;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.lucas.bank.ledger.domain.AccountType;
import com.lucas.bank.loan.adapter.out.LoanPOJO;
import com.lucas.bank.shared.staticInformation.StaticInformation;
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

    public static Boolean ofType(String hash, String sort)
    {
        return hash.startsWith(pkPrefix) && sort.startsWith(skPrefix);
    }

    @DynamoDBHashKey
    private String pk;

    @DynamoDBRangeKey

    private String sk;

    private String ledgerTransactionEntryId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = StaticInformation.LEDGER_ENTRIES_BY_LOAN_ID_GSI_INDEX)
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

    public static LedgerPOJO of(Long loanId){
        return LedgerPOJO.builder().loanId(loanId).build();
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
