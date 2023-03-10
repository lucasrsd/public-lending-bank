package com.lucas.bank.tax.application.port.in;

import com.lucas.bank.tax.application.port.out.TaxAggregate;

public interface CalculateTaxesUseCase {

    TaxAggregate calculate(CalculateTaxesCommand command);

}
