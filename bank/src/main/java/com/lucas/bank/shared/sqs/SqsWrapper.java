package com.lucas.bank.shared.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class SqsWrapper<T> {
    private final AmazonSQS amazonSQSClient;

    public SqsWrapper() {
        this.amazonSQSClient = AmazonSQSClientBuilder.defaultClient();
    }

    public void sendMessage(String queueTargetArn, T message) {

        var object = new Gson().toJson(message);
        this.amazonSQSClient.sendMessage(queueTargetArn, object);
    }
}
