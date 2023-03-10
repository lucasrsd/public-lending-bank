package com.lucas.bank.shared.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import com.lucas.bank.shared.staticInformation.StaticMessagingRouter;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class SqsWrapper {
    private final AmazonSQS amazonSQSClient;

    public SqsWrapper() {
        this.amazonSQSClient = AmazonSQSClientBuilder.defaultClient();
    }

    public void sendMessage(URI queueUrl, String routeName, Object message) {

        var object = new Gson().toJson(message);
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl.toString())
                .withMessageBody(object)
                .addMessageAttributesEntry(StaticMessagingRouter.ROUTER_KEY_NAME, new MessageAttributeValue()
                        .withDataType("String")
                        .withStringValue(routeName)
                );

        this.amazonSQSClient.sendMessage(request);
    }
}


