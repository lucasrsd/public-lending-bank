package com.lucas.bank.shared.dynamoDb;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DynamoDbQueryWrapper<T> {
    private DynamoDBMapper mapper;
    private Class<T> parserType;

    public DynamoDbQueryWrapper() {
    }

    public DynamoDbQueryWrapper(Class<T> parserType) {
        var client = AmazonDynamoDBClientBuilder.standard().withRegion(StaticInformation.AWS_REGION_STRING).build();
        this.mapper = new DynamoDBMapper(client);
        this.parserType = parserType;
    }

    public T get(T object) {
        return mapper.load(object);
    }


    public List<T> listByPkBeginsWithAndSk(String pkPrefix, String sk) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":prefix", new AttributeValue().withS(pkPrefix));
        eav.put(":sk", new AttributeValue().withS(sk));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with(pk, :prefix) and sk = :sk")
                .withExpressionAttributeValues(eav);

        return mapper.scan(this.parserType, scanExpression);
    }

    public List<T> listByPkBeginsWith(String pkPrefix) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":prefix", new AttributeValue().withS(pkPrefix));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with(pk, :prefix)")
                .withExpressionAttributeValues(eav);

        return mapper.scan(this.parserType, scanExpression);
    }

    public List<T> indexFullScan(String indexName) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withIndexName(indexName);

        return mapper.scan(this.parserType, scanExpression);
    }

    public List<T> scanIndexByBatchBlockAndStatus(String indexName, Integer batchBlock, String status) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":batchBlock", new AttributeValue().withN(batchBlock.toString()));
        eav.put(":loanState", new AttributeValue().withS(status));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withIndexName(indexName)
                .withFilterExpression("batchBlock = :batchBlock and loanState = :loanState")
                .withExpressionAttributeValues(eav);

        return mapper.scan(this.parserType, scanExpression);
    }

    public List<T> queryPkWithSkPrefix(T objectWithPk, String skPrefix) {

        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                .withAttributeValueList(new AttributeValue(skPrefix));

        final DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.withHashKeyValues(objectWithPk);
        queryExpression.withRangeKeyCondition("sk", condition);

        return mapper.query(this.parserType, queryExpression);
    }
}