package com.lucas.bank.shared.stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class StreamEventDTO {
    private final Map<String, String> keys;
    private final String operation;
    private final String jsonData;
}
