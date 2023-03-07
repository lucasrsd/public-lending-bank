package com.lucas.bank.transactions.adapter.out;

import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.taxes.domain.InstallmentDetails;
import com.lucas.bank.transactions.application.port.out.CreateTransactionPort;
import com.lucas.bank.transactions.application.port.out.LoadTransactionPort;
import com.lucas.bank.transactions.domain.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@PersistenceAdapter
class TransactionPersistenceAdapter implements CreateTransactionPort, LoadTransactionPort {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public Long createTransaction(Transaction transaction, PersistenceTransactionManager persistenceTransactionManager) {
        var transactionPOJO = transactionMapper.mapToPOJO(transaction);
        persistenceTransactionManager.addTransaction(transactionPOJO);
        return transactionPOJO.getTransactionId();
    }

    @Override
    public List<Transaction> forLoan(Long loanId) {
        var transactionPOJOS =  transactionRepository.queryPkWithSkPrefix(TransactionPOJO.of(loanId), TransactionPOJO.skPrefix);
        List<Transaction> transactions = new ArrayList<>();
        transactionPOJOS.forEach(i -> transactions.add(transactionMapper.mapToDomainEntity(i)));
        return transactions;
    }
}
