package com.lucas.bank.taxes.application.port.in;

import com.lucas.bank.loan.application.port.in.CreateLoanCommand;
import com.lucas.bank.taxes.application.port.out.TaxAggregate;

public interface CalculateTaxesUseCase {

    TaxAggregate calculate(CalculateTaxesCommand command);

}
