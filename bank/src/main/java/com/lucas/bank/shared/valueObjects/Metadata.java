package com.lucas.bank.shared.valueObjects;

import lombok.Value;

@Value
public class Metadata {
    private final Integer count;
    public static Metadata of(Integer count){
        return new Metadata(count);
    }
}
