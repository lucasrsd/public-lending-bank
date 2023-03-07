package com.lucas.bank.shared;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class PayableAmount {
    public BigDecimal amount;
    public BigDecimal paid;

    public static PayableAmount of(BigDecimal amount, BigDecimal paid){
        return new PayableAmount(amount, paid);
    }
}
