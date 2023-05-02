package com.lucas.bank.shared.secretManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecretDTO {
    private String username;
    private String password;
}
