package com.lucas.bank.installment.adapter.out;

import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.installment.application.port.out.CreateInstallmentPort;
import com.lucas.bank.installment.application.port.out.LoadInstallmentPort;
import com.lucas.bank.installment.domain.AmortizationType;
import com.lucas.bank.installment.domain.Installment;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
@PersistenceAdapter
class InstallmentPersistenceAdapter implements CreateInstallmentPort, LoadInstallmentPort {

    private final InstallmentRepository installmentRepository;
    private final InstallmentMapper installmentMapper;


    @Override
    public void createInstallment(AmortizationType amortizationType, Long loanId, List<Installment> installments) {
        List<InstallmentPOJO> installmentPOJOS = new ArrayList<>();
        installments.forEach(i -> installmentPOJOS.add(installmentMapper.mapToPOJO(amortizationType, loanId, i)));

        installmentRepository.batchPut(installmentPOJOS);
    }

    @Override
    public List<Installment> loadInstallments(Long loanId) {
        List<Installment> installments = new ArrayList<>();
        var installmentPOJO = installmentRepository.queryByPk(InstallmentPOJO.buildPk(loanId));

        Collections.sort(installmentPOJO, Comparator.comparing(InstallmentPOJO::getNumber));

        if (installmentPOJO != null && !installmentPOJO.isEmpty()) {
            installmentPOJO.forEach(i -> installments.add(installmentMapper.mapToDomainEntity(i)));
        }

        return installments;
    }
}
