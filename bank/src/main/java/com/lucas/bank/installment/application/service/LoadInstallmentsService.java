package com.lucas.bank.installment.application.service;

import com.lucas.bank.installment.application.port.out.InstallmentDetail;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.installment.application.port.in.LoadInstallmentsQuery;
import com.lucas.bank.installment.application.port.out.InstallmentAggregate;
import com.lucas.bank.installment.application.port.out.LoadInstallmentPort;
import com.lucas.bank.installment.domain.Installment;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@UseCase
public class LoadInstallmentsService implements LoadInstallmentsQuery {

    private final LoadInstallmentPort loadInstallmentPort;

    @Override
    public InstallmentAggregate loadInstallments(Long loanId) {
        var installments = loadInstallmentPort.loadInstallments(loanId);

        return InstallmentAggregate
                .builder()
                .installments(installments)
                .details(details(installments))
                .build();
    }

    private InstallmentDetail details(List<Installment> installments) {
        BigDecimal principalSum = BigDecimal.ZERO;
        BigDecimal interestSum = BigDecimal.ZERO;
        BigDecimal installmentSum = BigDecimal.ZERO;
        BigDecimal taxesSum = BigDecimal.ZERO;
        Map<String, BigDecimal> taxesByType = new HashMap<>();

        for (Installment installment : installments) {

            principalSum = principalSum.add(installment.getPrincipalAmount());
            interestSum = interestSum.add(installment.getInterestAmount());
            installmentSum = installmentSum.add(installment.getInstallmentAmount());
            taxesSum = taxesSum.add(installment.getTaxAmount());

            if (installment.getTaxComposition() != null) {
                for (Map.Entry<String, BigDecimal> item : installment.getTaxComposition().entrySet()) {

                    if (taxesByType.containsKey(item.getKey()))
                        taxesByType.put(item.getKey(), taxesByType.get(item.getKey()).add(item.getValue()));
                    else
                        taxesByType.put(item.getKey(), item.getValue());
                }
            }
        }

        InstallmentDetail details = InstallmentDetail
                .builder()
                .installmentsTotalAmount(installmentSum)
                .principalTotalAmount(principalSum)
                .interestTotalAmount(interestSum)
                .taxTotalAmount(taxesSum)
                .taxes(taxesByType)
                .build();

        return details;
    }
}
