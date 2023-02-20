package com.lucas.bank.shared.dynamoDb;

import com.lucas.bank.shared.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DynamoDbParallelScanWrapper<T> {

    private final Logger LOG = LoggerFactory.getLogger(DynamoDbParallelScanWrapper.class);

    private DynamoDbClient ddb;
    private String tableName;
    private Class<T> parserType;
    public DynamoDbParallelScanWrapper() {
    }

    public DynamoDbParallelScanWrapper(Class<T> parserType, String tableName) {
        this.tableName = tableName;
        this.parserType = parserType;
        this.ddb = DynamoDbClient.builder().region(StaticInformation.AWS_REGION).build();
    }

    private ScanRequest buildRequest(String indexName, int totalSegments, int segment, Map<String, AttributeValue> lastEvaluatedKey) {
        var requestBuilder = ScanRequest.builder().tableName(this.tableName).indexName(indexName).totalSegments(totalSegments).segment(segment);

        if (lastEvaluatedKey != null) {
            requestBuilder.exclusiveStartKey(lastEvaluatedKey);
        }

        return requestBuilder.build();
    }

    public List<T> fullIndexScan(String indexName, int totalSegments, int segment) {


        var request = buildRequest(indexName, totalSegments, segment, null);
        var scanResponse = this.ddb.scan(request);

        List<Map<String, AttributeValue>> items = new ArrayList<>();

        var count = 0;

        do {
            LOG.info("Executing new paginated result {}", count++);
            scanResponse = this.ddb.scan(buildRequest(indexName, totalSegments, segment, scanResponse.lastEvaluatedKey()));
            items.addAll(scanResponse.items());
        }
        while (scanResponse.hasLastEvaluatedKey());

        T current = null;
        List<T> response = new ArrayList<>();

        for (Map<String, AttributeValue> item : items) {
            var parsed = TableSchema.fromClass(this.parserType).mapToItem(item);
            response.add(parsed);
        }
        return response;
    }
}