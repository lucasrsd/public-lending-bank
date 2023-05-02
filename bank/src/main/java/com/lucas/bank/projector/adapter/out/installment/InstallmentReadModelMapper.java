package com.lucas.bank.projector.adapter.out.installment;

import com.lucas.bank.installment.adapter.out.InstallmentDataPOJO;
import com.lucas.bank.installment.adapter.out.InstallmentPOJO;
import com.lucas.bank.loan.adapter.out.LoanPOJO;
import com.lucas.bank.projector.adapter.out.loan.LoanReadModel;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.lucas.bank.shared.staticInformation.StaticFieldName.ADDITIONAL_IOF_FIELD;
import static com.lucas.bank.shared.staticInformation.StaticFieldName.DAILY_IOF_FIELD;

@Component
public class InstallmentReadModelMapper {

    public List<InstallmentReadModel> mapToDomainEntity(InstallmentDataPOJO installmentDataPOJO) {

        List<InstallmentReadModel> installments = new ArrayList<>();

        for (InstallmentPOJO pojo : installmentDataPOJO.getInstallments()) {

            BigDecimal additionalIof = null;
            BigDecimal dailyIof = null;

            if (pojo.getTaxComposition() != null) {
                if (pojo.getTaxComposition().containsKey(ADDITIONAL_IOF_FIELD))
                    additionalIof = pojo.getTaxComposition().get(ADDITIONAL_IOF_FIELD);
                if (pojo.getTaxComposition().containsKey(DAILY_IOF_FIELD))
                    dailyIof = pojo.getTaxComposition().get(DAILY_IOF_FIELD);
            }

            var installmentRm = InstallmentReadModel
                    .builder()
                    .loanId(installmentDataPOJO.getLoanId())
                    .number(pojo.getNumber())
                    .amortizationType(pojo.getAmortizationType())
                    .installmentState(pojo.getInstallmentState())
                    .dueDate(DateTimeUtil.from(pojo.getDueDate()))
                    .paymentDate(DateTimeUtil.from(pojo.getPaymentDate()))
                    .principalAmount(pojo.getPrincipalAmount())
                    .interestAmount(pojo.getInterestAmount())
                    .installmentAmount(pojo.getInstallmentAmount())
                    .taxAmount(pojo.getTaxAmount())
                    .remainingBalance(pojo.getRemainingBalance())
                    .paidPrincipalAmount(pojo.getPaidPrincipalAmount())
                    .paidInterestAmount(pojo.getPaidInterestAmount())
                    .paidTaxAmount(pojo.getPaidTaxAmount())
                    .taxAdditionalIof(additionalIof)
                    .taxDailyIof(dailyIof)
                    .build();


            installments.add(installmentRm);
        }

        return installments;
    }
}