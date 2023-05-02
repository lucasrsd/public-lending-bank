package com.lucas.bank.shared.dynamoDb;

import com.lucas.bank.shared.adapters.AtomicCounter;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

@Component
public class DynamoDbAtomicCounter implements AtomicCounter {

    private final Logger log = LoggerFactory.getLogger(DynamoDbAtomicCounter.class);
    private DynamoDbClient client;

    public DynamoDbAtomicCounter() {
        this.client =  DynamoDbClient.builder().region(Region.of(StaticInformation.getAwsRegion())).build();
    }

    public Long generate() {

        // ToDo - Decide on properly distributed ID generation

        var attributeUpdateValue = AttributeValueUpdate
                .builder()
                .value(AttributeValue.fromN("" + 1))
                .action(AttributeAction.ADD)
                .build();

       var updateItemRequest = UpdateItemRequest.builder()
                .tableName(StaticInformation.CONFIGURATION_TABLE_NAME)
                .key(Map.of("key", AttributeValue.fromS("atomic-counter"), "type", AttributeValue.fromS("global-unique-id")))
                .attributeUpdates(Map.of("counter", attributeUpdateValue))
                .returnValues(ReturnValue.ALL_NEW)
                .returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
                .build();

        var updateResult = this.client.updateItem(updateItemRequest);

        log.info("Atomic counter generate transaction executed, consumed capacity: {}", updateResult.consumedCapacity().capacityUnits());

        return StaticInformation.getRegionIdPrefix() +  Long.parseLong(updateResult.attributes().get("counter").n());
    }
}