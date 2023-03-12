package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.installment.application.port.out.UpdateInstallmentPort;
import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.installment.application.port.out.CreateInstallmentPort;
import com.lucas.bank.installment.application.port.out.LoadInstallmentPort;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@PersistenceAdapter
class InstallmentPersistenceAdapter implements CreateInstallmentPort, LoadInstallmentPort, UpdateInstallmentPort {

    private final InstallmentRepository installmentRepository;
    private final InstallmentMapper installmentMapper;


    @Override
    public void createInstallment(AmortizationType amortizationType, Long loanId, List<Installment> installments, UnitOfWork unitOfWork) {
        var installmentPOJO = installmentMapper.mapToPOJO(amortizationType, loanId, installments);
        unitOfWork.addTransaction(installmentPOJO);
    }

    @Override
    public List<Installment> loadInstallments(Long loanId) {
        var installmentPOJO = installmentRepository.get(InstallmentDataPOJO.of(loanId));
        return installmentMapper.mapToDomainEntity(installmentPOJO.getInstallments());
    }

    @Override
    public void updateInstallments(Long loanId, AmortizationType amortizationType, List<Installment> installments, UnitOfWork unitOfWork) {
        var installmentPOJO = installmentMapper.mapToPOJO(amortizationType, loanId, installments);
        unitOfWork.addTransaction(installmentPOJO);
    }
}
