package com.lucas.bank.installment.domain;

import com.lucas.bank.loan.domain.AmortizationType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnsupportedAmortizationException extends ResponseStatusException {
    public UnsupportedAmortizationException(AmortizationType amortizationType) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Amortization type: " + amortizationType + " is not supported");
    }
}
