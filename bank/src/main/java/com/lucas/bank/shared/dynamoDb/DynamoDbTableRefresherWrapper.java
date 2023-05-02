package com.lucas.bank.shared.dynamoDb;


import com.lucas.bank.shared.staticInformation.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.paginators.ScanIterable;

import java.util.Map;

@Component
public class DynamoDbTableRefresherWrapper {

    private final Logger log = LoggerFactory.getLogger(DynamoDbTableRefresherWrapper.class);
    private DynamoDbClient client;
    private final String versionFieldName = "internal_version";

    public DynamoDbTableRefresherWrapper() {
        this.client = DynamoDbClient.builder().region(Region.of(StaticInformation.getAwsRegion())).build();
    }

    public void refreshTableVersion(Long versionNumber) {
        ScanRequest request = ScanRequest
                .builder()
                .tableName(StaticInformation.SINGLE_TABLE_NAME)
                .returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
                .build();

        var pageCount = 0;
        var itemCount = 0;

        log.info("Starting scan");

        ScanIterable response = client.scanPaginator(request);

        for (ScanResponse page : response) {
            pageCount++;

            for (Map<String, AttributeValue> item : page.items()) {

                if (item.containsKey(versionFieldName)) {
                    if (item.get(versionFieldName).n().equals(versionNumber.toString())) {
                        continue;
                    }
                }

                var attributeUpdateValue = AttributeValueUpdate
                        .builder()
                        .value(AttributeValue.fromN(versionNumber.toString()))
                        .action(AttributeAction.PUT)
                        .build();

                var updateItemRequest = UpdateItemRequest.builder()
                        .tableName(StaticInformation.SINGLE_TABLE_NAME)
                        .key(Map.of("pk", AttributeValue.fromS(item.get("pk").s()), "sk", AttributeValue.fromS(item.get("sk").s())))
                        .attributeUpdates(Map.of(versionFieldName, attributeUpdateValue))
                        .returnValues(ReturnValue.ALL_NEW)
                        .returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
                        .build();

                var updateResult = client.updateItem(updateItemRequest);

                log.info("Item updated, page: {}, item: {} > page CC: {}, update CC: {}", pageCount, itemCount, page.consumedCapacity().capacityUnits(), updateResult.consumedCapacity().capacityUnits());

                itemCount++;
            }
        }
    }

}