package com.lucas.bank.shared.dynamoDb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.lucas.bank.shared.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DynamoDbTransaction {
    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public DynamoDbTransaction() {
        this.client = AmazonDynamoDBClientBuilder.standard().withRegion(StaticInformation.AWS_REGION_STRING).build();
        this.mapper = new DynamoDBMapper(client);
    }

    public void commitTransaction(List<Object> items){
        TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();

        for (Object obj : items){
            transactionWriteRequest.addPut(obj);
        }

        this.mapper.transactionWrite(transactionWriteRequest);
    }
}
