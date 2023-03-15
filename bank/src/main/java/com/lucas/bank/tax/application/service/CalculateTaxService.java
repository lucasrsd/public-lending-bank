package com.lucas.bank.tax.application.service;

import com.lucas.bank.shared.ParameterConsistencyException;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.tax.application.port.in.CalculateTaxesCommand;
import com.lucas.bank.tax.application.port.in.CalculateTaxesUseCase;
import com.lucas.bank.tax.application.port.out.TaxAggregate;
import com.lucas.bank.tax.domain.IOF;
import com.lucas.bank.tax.domain.InstallmentTax;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@UseCase
public class CalculateTaxService implements CalculateTaxesUseCase {
    @Override
    public TaxAggregate calculate(CalculateTaxesCommand command) {
        if (command.getTaxType().equals("IOF")) {
            var iofCalculator = IOF.builder().build();

            var iofResult = iofCalculator.calculate(command.getDisbusementDate(), command.getInstallmentDetails());

            Map<Integer, BigDecimal> taxInstallmentAmount = new HashMap<>();
            Map<Integer, Map<String, BigDecimal>> composition = new HashMap<>();
            BigDecimal totalTax = BigDecimal.ZERO;

            for (InstallmentTax tax : iofResult) {
                taxInstallmentAmount.put(tax.getNumber(), tax.getTotalTaxAmount());
                composition.put(tax.getNumber(), tax.getComposition());
                totalTax = totalTax.add(tax.getTotalTaxAmount());
            }

            return TaxAggregate
                    .builder()
                    .taxAmount(taxInstallmentAmount)
                    .composition(composition)
                    .totalTax(totalTax)
                    .build();
        }

        throw new ParameterConsistencyException("Tax type " + command.getTaxType() + " not supported");
    }
}
