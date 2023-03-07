package com.lucas.bank.installment.application.port.out;

import com.lucas.bank.installment.domain.Installment;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class InstallmentAggregate {

    private final List<Installment> installments;
    private final InstallmentDetail details;
}
