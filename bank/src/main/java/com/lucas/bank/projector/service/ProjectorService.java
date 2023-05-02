package com.lucas.bank.projector.service;

import com.google.gson.Gson;
import com.lucas.bank.account.adapter.out.AccountPOJO;
import com.lucas.bank.installment.adapter.out.InstallmentDataPOJO;
import com.lucas.bank.ledger.adapter.out.LedgerPOJO;
import com.lucas.bank.loan.adapter.out.LoanPOJO;
import com.lucas.bank.projector.application.port.in.CreateProjectionUseCase;
import com.lucas.bank.projector.application.port.out.*;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.stream.StreamEventDTO;
import com.lucas.bank.transaction.adapter.out.TransactionPOJO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RequiredArgsConstructor
@UseCase
public class ProjectorService implements CreateProjectionUseCase {
    private final Logger log = LoggerFactory.getLogger(ProjectorService.class);
    private final AccountProjectionPort accountProjectionPort;
    private final LoanProjectionPort loanProjectionPort;
    private final InstallmentProjectionPort installmentProjectionPort;
    private final TransactionProjectionPort transactionProjectionPort;
    private final LedgerProjectionPort ledgerProjectionPort;

    @Override
    public void project(StreamEventDTO event) {
        log.info("Projector message received: {}", new Gson().toJson(event));

        var pk = event.getKeys().get("pk");
        var sk = event.getKeys().get("sk");

        if (AccountPOJO.ofType(pk, sk)) {
            processAccount(event.getOperation(), pk, pk, new Gson().fromJson(event.getJsonData(), AccountPOJO.class));
        }

        if (InstallmentDataPOJO.ofType(pk, sk)) {
            processInstallment(event.getOperation(), pk, pk, new Gson().fromJson(event.getJsonData(), InstallmentDataPOJO.class));
        }

        if (LedgerPOJO.ofType(pk, sk)) {
            processLedger(event.getOperation(), pk, pk, new Gson().fromJson(event.getJsonData(), LedgerPOJO.class));
        }

        if (LoanPOJO.ofType(pk, sk)) {
            processLoan(event.getOperation(), pk, pk, new Gson().fromJson(event.getJsonData(), LoanPOJO.class));
        }

        if (TransactionPOJO.ofType(pk, sk)) {
            processTransaction(event.getOperation(), pk, pk, new Gson().fromJson(event.getJsonData(), TransactionPOJO.class));
        }
    }

    private void processAccount(String operation, String pk, String sk, AccountPOJO account) {
        log.info("Processing account, operation: {}, pk: {}, sk: {}, object: {}", operation, pk, sk, new Gson().toJson(account));
        accountProjectionPort.project(account);
    }

    private void processInstallment(String operation, String pk, String sk, InstallmentDataPOJO installment) {
        log.info("Processing installment, operation: {}, pk: {}, sk: {}, object: {}", operation, pk, sk, new Gson().toJson(installment));
        installmentProjectionPort.project(installment);
    }

    private void processLedger(String operation, String pk, String sk, LedgerPOJO ledger) {
        log.info("Processing ledger, operation: {}, pk: {}, sk: {}, object: {}", operation, pk, sk, new Gson().toJson(ledger));
        ledgerProjectionPort.project(ledger);
    }

    private void processLoan(String operation, String pk, String sk, LoanPOJO loan) {
        log.info("Processing loan, operation: {}, pk: {}, sk: {}, object: {}", operation, pk, sk, new Gson().toJson(loan));
        loanProjectionPort.project(loan);
    }

    private void processTransaction(String operation, String pk, String sk, TransactionPOJO transaction) {
        log.info("Processing transaction, operation: {}, pk: {}, sk: {}, object: {}", operation, pk, sk, new Gson().toJson(transaction));
        transactionProjectionPort.project(transaction);
    }
}
