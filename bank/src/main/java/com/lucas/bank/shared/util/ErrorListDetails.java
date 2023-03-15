package com.lucas.bank.shared.util;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@AllArgsConstructor
public class ErrorListDetails {
    private LocalDateTime timestamp;
    private List<ErrorItem> errors;
    private String details;
}