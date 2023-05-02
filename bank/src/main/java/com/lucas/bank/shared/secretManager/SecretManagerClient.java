package com.lucas.bank.shared.secretManager;

import com.lucas.bank.shared.staticInformation.StaticInformation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecretManagerClient {
    private final SecretsManagerPullConfig secretsManagerPullConfig;
    private final Logger log = LoggerFactory.getLogger(SecretManagerClient.class);


    public SecretDTO getDatabaseAuth() {
        try {
            log.info("Retrieving database secret...");
            return secretsManagerPullConfig.getSecret(StaticInformation.DATABASE_SECRET_NAME(), SecretDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
