package com.lucas.bank.shared.dynamoDb;

import com.lucas.bank.shared.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
public class DynamoDbWrapper<T> {

    private final Logger LOG = LoggerFactory.getLogger(DynamoDbWrapper.class);
    private static final Integer BATCH_SIZE_LIMIT = 25;
    private DynamoDbClient ddb;
    private DynamoDbEnhancedClient enhancedClient;
    private String tableName;
    private Class<T> parserType;

    public DynamoDbWrapper() {
    }

    public DynamoDbWrapper(Class<T> parserType, String tableName) {
        this.tableName = tableName;
        this.parserType = parserType;
        this.ddb = DynamoDbClient.builder().region(StaticInformation.AWS_REGION).build();
        this.enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();
    }

    private QueryRequest buildQueryRequestByPk(AttributeValue pkFilter, Map<String, AttributeValue> startKey) {
        var builder = QueryRequest
                .builder()
                .tableName(this.tableName)
                .keyConditionExpression("#pk = :pk")
                .expressionAttributeNames(Map.of("#pk", "pk"))
                .expressionAttributeValues(Map.of(":pk", pkFilter))
                .scanIndexForward(false);

        if (startKey != null) builder.exclusiveStartKey(startKey);

        return builder.build();
    }

    public List<T> queryByPk(String pk) {

        AttributeValue filterAttribute = AttributeValue.builder().s(pk).build();

        var queryRequest = buildQueryRequestByPk(filterAttribute, null);

        QueryResponse queryResponse = this.ddb.query(queryRequest);

        List<Map<String, AttributeValue>> items = new ArrayList<>();

        var count = 0;

        do {
            LOG.info("Executing new paginated result {}", count++);
            queryResponse = this.ddb.query(buildQueryRequestByPk(filterAttribute, queryResponse.lastEvaluatedKey()));
            items.addAll(queryResponse.items());
        }
        while (queryResponse.hasLastEvaluatedKey());

        T current = null;
        List<T> response = new ArrayList<>();

        for (Map<String, AttributeValue> item : items) {
            var parsed = TableSchema.fromClass(this.parserType).mapToItem(item);
            response.add(parsed);
        }
        return response;
    }

    public T get(String pk, String sk) {
        var pkAttr = AttributeValue.builder().s(pk).build();
        var skAttr = AttributeValue.builder().s(sk).build();
        var getItemRequest = GetItemRequest.builder().tableName(tableName).key(Map.of("pk", pkAttr, "sk", skAttr)).build();
        GetItemResponse item = this.ddb.getItem(getItemRequest);
        return TableSchema.fromClass(this.parserType).mapToItem(item.item());
    }

    public void put(T object) {
        var workTable = enhancedClient.table(this.tableName, TableSchema.fromBean(this.parserType));
        workTable.putItem(object);
    }

    public void batchPut(List<T> objects) {

        var customerMappedTable = enhancedClient.table(this.tableName, TableSchema.fromBean(this.parserType));
        var batch = WriteBatch.builder(this.parserType).mappedTableResource(customerMappedTable);

        var batchSize = 0;

        for (T obj : objects) {
            batch.addPutItem(builder -> builder.item(obj));
            batchSize++;
            if (batchSize >= BATCH_SIZE_LIMIT) {
                executeBatch(batch);
                batchSize = 0;
                batch = WriteBatch.builder(this.parserType).mappedTableResource(customerMappedTable);
            }
        }
        if (batchSize > 0) {
            executeBatch(batch);
        }
    }

    private void executeBatch(WriteBatch.Builder<T> batch) {
        var batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder().writeBatches(batch.build()).build();

        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
    }
}