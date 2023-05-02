package com.lucas.bank.projector.application.port.out;

import com.lucas.bank.account.adapter.out.AccountPOJO;
import com.lucas.bank.loan.adapter.out.LoanPOJO;

public interface LoanProjectionPort {
    void project(LoanPOJO loan);
}
