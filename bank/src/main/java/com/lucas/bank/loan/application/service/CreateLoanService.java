package com.lucas.bank.loan.application.service;

import com.lucas.bank.account.application.port.in.LoadAccountQuery;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.loan.domain.LoanState;
import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.ParameterConsistencyException;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.installment.application.port.in.CreateInstallmentCommand;
import com.lucas.bank.installment.application.port.in.CreateInstallmentUseCase;
import com.lucas.bank.interest.domain.Interest;
import com.lucas.bank.interest.domain.InterestFrequency;
import com.lucas.bank.loan.application.port.in.CreateLoanCommand;
import com.lucas.bank.loan.application.port.in.CreateLoanUseCase;
import com.lucas.bank.loan.application.port.out.CreateLoanPort;
import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.tax.application.port.in.CalculateTaxesCommand;
import com.lucas.bank.tax.application.port.in.CalculateTaxesUseCase;
import com.lucas.bank.tax.application.port.out.TaxAggregate;
import com.lucas.bank.tax.domain.InstallmentDetails;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@UseCase
public class CreateLoanService implements CreateLoanUseCase {

    private final CreateLoanPort createLoanPort;
    private final LoadAccountQuery loadAccountQuery;
    private final CreateInstallmentUseCase createInstallmentUseCase;
    private final CalculateTaxesUseCase calculateTaxesUseCase;
    private final AccrualUseCase accrualUseCase;

    public static Loan of(Long accountId, String type, BigDecimal amount, BigDecimal interestRate, String interestFrequency, Integer term) {
        var interest = Interest.of(interestRate, InterestFrequency.valueOf(interestFrequency));
        return Loan.builder().accountId(accountId).type(AmortizationType.valueOf(type)).amount(amount).state(LoanState.DRAFT).term(term).interest(interest).creationDate(DateTimeUtil.nowWithTimeZone()).build();
    }

    @Override
    public Long createLoan(CreateLoanCommand command, UnitOfWork unitOfWork) {

        LocalDateTime disbursementDate = command.getDisbursementDate() == null
                ? DateTimeUtil.nowWithTimeZone()
                : command.getDisbursementDate();

        var account = loadAccountQuery.loadAccount(command.getAccountId());

        var interest = Interest.of(command.getInterestRate(), InterestFrequency.valueOf(command.getInterestFrequency()));

        var calculationInterest = Interest.cloneAndConvert(interest, InterestFrequency.MONTH); // Only monthly interest accepted for now

        var calculationInterestRate = calculationInterest.getRateToCalculate();

        var taxes = calculateTaxesIfPresent(command.getAmount(), command.getTerm(), AmortizationType.valueOf(command.getAmortizationType()).name(), disbursementDate, command.getTax(), calculationInterestRate);

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (taxes == null) {
            totalAmount = command.getAmount();
        } else {
            totalAmount = command.getAmount().add(taxes.getTotalTax());

            // ToDo - use gross up
            taxes = calculateTaxesIfPresent(totalAmount, command.getTerm(), AmortizationType.valueOf(command.getAmortizationType()).name(), disbursementDate, command.getTax(), calculationInterestRate);
        }

        var daysFromDisbursement = ChronoUnit.DAYS.between(DateTimeUtil.convertToMidnight(disbursementDate),
                DateTimeUtil.convertToMidnight(DateTimeUtil.nowWithTimeZone()));

        if (daysFromDisbursement < 0){
            throw new ParameterConsistencyException("Disbursement date cannot be in the future.");
        }

        LocalDateTime lastAccrualDate = null;
        BigDecimal accruedInterest = null;

        if(daysFromDisbursement > 0L){
            accruedInterest = accrualUseCase.calculateDailyAccrual(totalAmount, interest, daysFromDisbursement);
            lastAccrualDate = DateTimeUtil.nowWithTimeZone();
        }

        totalAmount = totalAmount.setScale(StaticInformation.TRANSACTION_PRECISION_SCALE, StaticInformation.TRANSACTION_ROUNDING_MODE);

        var loan = Loan
                .builder()
                .accountId(account.getAccountId())
                .type(AmortizationType.valueOf(command.getAmortizationType()))
                .amount(totalAmount)
                .state(LoanState.DRAFT)
                .interest(interest)
                .term(command.getTerm())
                .lastAccrualDate(lastAccrualDate)
                .accruedInterest(accruedInterest)
                .creationDate(DateTimeUtil.nowWithTimeZone())
                .disbursementDate(disbursementDate)
                .build();

        var createdLoan = createLoanPort.createLoan(loan, unitOfWork);

        var createInstallmentCommand = CreateInstallmentCommand
                .builder()
                .loanId(createdLoan)
                .amount(totalAmount)
                .term(loan.getTerm())
                .rate(calculationInterestRate)
                .amortizationType(loan.getType().name())
                .taxes(taxes)
                .disbursementDate(disbursementDate)
                .build();

        createInstallmentUseCase.create(createInstallmentCommand, unitOfWork);

        return createdLoan;
    }

    private TaxAggregate calculateTaxesIfPresent(BigDecimal amount, Integer term, String loanType, LocalDateTime disbursementDate, String taxType, BigDecimal rate) {

        if (taxType == null) return null;

        var previewInstallmentCommand = CreateInstallmentCommand
                .builder()
                .amount(amount)
                .term(term)
                .rate(rate)
                .amortizationType(loanType)
                .taxes(null)
                .disbursementDate(disbursementDate)
                .build();

        List<InstallmentDetails> installmentDetails = new ArrayList<>();

        var installmentsPreview = createInstallmentUseCase.preview(previewInstallmentCommand);

        for (Installment installment : installmentsPreview) {

            InstallmentDetails installmentDetail = InstallmentDetails
                    .builder()
                    .dueDate(installment.getDueDate())
                    .number(installment.getNumber())
                    .principalAmount(installment.getPrincipalAmount())
                    .build();

            installmentDetails.add(installmentDetail);
        }

        CalculateTaxesCommand taxesCommand = CalculateTaxesCommand
                .builder()
                .taxType(taxType)
                .disbusementDate(disbursementDate)
                .installmentDetails(installmentDetails)
                .build();

        return calculateTaxesUseCase.calculate(taxesCommand);
    }
}
