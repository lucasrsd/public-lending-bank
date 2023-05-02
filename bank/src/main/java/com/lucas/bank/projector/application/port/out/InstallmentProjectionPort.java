package com.lucas.bank.projector.application.port.out;

import com.lucas.bank.installment.adapter.out.InstallmentDataPOJO;

public interface InstallmentProjectionPort {
    void project(InstallmentDataPOJO installmentDataPOJO);
}
