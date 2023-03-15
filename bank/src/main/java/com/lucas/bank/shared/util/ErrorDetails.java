package com.lucas.bank.shared.util;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;


@Value
@AllArgsConstructor
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String details;
}