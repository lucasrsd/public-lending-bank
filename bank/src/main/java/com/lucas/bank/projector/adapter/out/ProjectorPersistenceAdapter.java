package com.lucas.bank.projector.adapter.out;

import com.lucas.bank.account.adapter.out.AccountPOJO;
import com.lucas.bank.installment.adapter.out.InstallmentDataPOJO;
import com.lucas.bank.ledger.adapter.out.LedgerPOJO;
import com.lucas.bank.loan.adapter.out.LoanPOJO;
import com.lucas.bank.projector.adapter.out.account.AccountReadModelMapper;
import com.lucas.bank.projector.adapter.out.account.AccountReadModelRepository;
import com.lucas.bank.projector.adapter.out.installment.InstallmentReadModelMapper;
import com.lucas.bank.projector.adapter.out.installment.InstallmentReadModelRepository;
import com.lucas.bank.projector.adapter.out.ledger.LedgerReadModelMapper;
import com.lucas.bank.projector.adapter.out.ledger.LedgerReadModelRepository;
import com.lucas.bank.projector.adapter.out.transaction.TransactionReadModelMapper;
import com.lucas.bank.projector.adapter.out.transaction.TransactionReadModelRepository;
import com.lucas.bank.projector.adapter.out.loan.LoanReadModelMapper;
import com.lucas.bank.projector.adapter.out.loan.LoanReadModelRepository;
import com.lucas.bank.projector.application.port.out.*;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.transaction.adapter.out.TransactionPOJO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@PersistenceAdapter
class ProjectorPersistenceAdapter implements AccountProjectionPort, LoanProjectionPort, InstallmentProjectionPort, TransactionProjectionPort, LedgerProjectionPort {

    private final Logger log = LoggerFactory.getLogger(ProjectorPersistenceAdapter.class);

    private final AccountReadModelMapper accountReadModelMapper;
    private final AccountReadModelRepository accountReadModelRepository;

    private final LoanReadModelMapper loanReadModelMapper;
    private final LoanReadModelRepository loanReadModelRepository;

    private final InstallmentReadModelMapper installmentReadModelMapper;
    private final InstallmentReadModelRepository installmentReadModelRepository;

    private final TransactionReadModelMapper transactionReadModelMapper;
    private final TransactionReadModelRepository transactionReadModelRepository;

    private final LedgerReadModelMapper ledgerReadModelMapper;
    private final LedgerReadModelRepository ledgerReadModelRepository;

    @Override
    public void project(AccountPOJO account) {
        accountReadModelRepository.save(accountReadModelMapper.mapToDomainEntity(account));
    }

    @Override
    public void project(LoanPOJO loan) {
        loanReadModelRepository.save(loanReadModelMapper.mapToDomainEntity(loan));
    }

    @Override
    public void project(InstallmentDataPOJO installmentDataPOJO) {
        installmentReadModelRepository.save(installmentDataPOJO.getLoanId(), installmentReadModelMapper.mapToDomainEntity(installmentDataPOJO));
    }

    @Override
    public void project(TransactionPOJO transactionPOJO) {
        transactionReadModelRepository.save(transactionReadModelMapper.mapToDomainEntity(transactionPOJO));
    }

    @Override
    public void project(LedgerPOJO ledgerPOJO) {
        ledgerReadModelRepository.save(ledgerReadModelMapper.mapToDomainEntity(ledgerPOJO));
    }
}
