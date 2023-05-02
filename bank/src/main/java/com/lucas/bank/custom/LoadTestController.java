package com.lucas.bank.custom;

import com.lucas.bank.account.application.port.in.CreateAccountCommand;
import com.lucas.bank.account.application.port.in.CreateAccountUseCase;
import com.lucas.bank.loan.adapter.in.contracts.CreateLoanRequest;
import com.lucas.bank.loan.application.port.in.CreateLoanUseCase;
import com.lucas.bank.shared.adapters.WebAdapter;
import com.lucas.bank.shared.dynamoDb.DynamoDbTableRefresherWrapper;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.transaction.application.port.in.DisburseLoanUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/load-test")
public class LoadTestController {
    private final Logger log = LoggerFactory.getLogger(LoadTestController.class);

    private final CreateLoanUseCase createLoanUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final DisburseLoanUseCase disburseLoanUseCase;

    @PostMapping
    void create(Long loanId) {

        if (1 == 1)
            throw new RuntimeException("DISABLED");

        for (int x = 0; x < 10000; x++){
            var transaction = UnitOfWork.newInstance();
            var customerCommand = CreateAccountCommand
                    .builder()
                    .holderName("Load test - " + DateTimeUtil.to(LocalDateTime.now()).toString())
                    .holderBirthDate(LocalDateTime.now().plusYears(-20))
                    .build();

            var customer = createAccountUseCase.createAccount(customerCommand, transaction);

            log.info("Customer created: {}", customer.getAccountId());

            transaction.commit();

            var loanRequest = new CreateLoanRequest();
            loanRequest.setAccountId(customer.getAccountId());
            loanRequest.setAmount(new BigDecimal(ThreadLocalRandom.current().nextInt(10, 100_000_000)));

            if (LocalDateTime.now().getSecond() % 2 == 0)
                loanRequest.setTax("IOF");

            loanRequest.setTerm(ThreadLocalRandom.current().nextInt(1, 72));

            if (LocalDateTime.now().getSecond() % 2 == 0)
                loanRequest.setAmortizationType("PRICE");
            else
                loanRequest.setAmortizationType("SAC");

            var disbursementDate = LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(25, 35) * -1);
            loanRequest.setDisbursementDate(disbursementDate.format(StaticInformation.DATE_FORMATTER));

            var rndTerm = ThreadLocalRandom.current().nextInt(1, 3);

            if (rndTerm == 1) {
                loanRequest.setInterestRate(new BigDecimal(ThreadLocalRandom.current().nextInt(1, 20)));
                loanRequest.setInterestFrequency("YEAR");
            }
            else if (rndTerm == 2) {
                loanRequest.setInterestRate(new BigDecimal(ThreadLocalRandom.current().nextInt(1, 20)));
                loanRequest.setInterestFrequency("MONTH");
            }
            else if (rndTerm == 3) {
                loanRequest.setInterestRate(new BigDecimal(ThreadLocalRandom.current().nextInt(1, 3)));
                loanRequest.setInterestFrequency("DAY");
            }

            var loan = createLoanUseCase.createLoan(loanRequest.mapToCommand(), transaction);

            log.info("Loan created: {}", loan);

            transaction.commit();

            disburseLoanUseCase.disburse(loan, transaction);

            transaction.commit();

            log.info("Loan disbursed");
        }
    }
}
