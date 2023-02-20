package com.lucas.bank.installment.application.service;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.installment.application.port.in.CreateInstallmentCommand;
import com.lucas.bank.installment.application.port.in.CreateInstallmentUseCase;
import com.lucas.bank.installment.application.port.out.CreateInstallmentPort;
import com.lucas.bank.installment.domain.AmortizationType;
import com.lucas.bank.installment.domain.InstallmentFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class InstallmentService implements CreateInstallmentUseCase {

    private final CreateInstallmentPort createInstallmentPort;

    @Override
    public void create(CreateInstallmentCommand command) {
        if (command.getLoanId() == null)
            throw new RuntimeException("Loan Id null");

        var installments = preview(command);
        createInstallmentPort.createInstallment(AmortizationType.valueOf(command.getAmortizationType()), command.getLoanId(), installments);
    }

    @Override
    public List<Installment> preview(CreateInstallmentCommand command) {
        var installmentCalculator = InstallmentFactory.of(AmortizationType.valueOf(command.getAmortizationType()));
        return installmentCalculator.calculateAmortization(command.getDisbursementDate(), command.getRate(), command.getTerm(), command.getAmount(), command.getTaxes());
    }
}
