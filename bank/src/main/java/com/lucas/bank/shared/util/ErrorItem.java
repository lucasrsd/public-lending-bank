package com.lucas.bank.shared.util;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorItem {
    private String field;
    private String type;
    private String message;
}
