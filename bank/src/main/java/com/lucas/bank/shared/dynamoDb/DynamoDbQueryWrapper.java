package com.lucas.bank.shared.dynamoDb;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DynamoDbQueryWrapper<T> {

    private final Logger LOG = LoggerFactory.getLogger(DynamoDbQueryWrapper.class);
    private DynamoDBMapper mapper;
    private Class<T> parserType;

    public DynamoDbQueryWrapper() {
    }

    public DynamoDbQueryWrapper(Class<T> parserType) {
        var client = AmazonDynamoDBClientBuilder.standard().withRegion(StaticInformation.getAwsRegion()).build();
        this.mapper = new DynamoDBMapper(client);
        this.parserType = parserType;
    }

    public T get(T object) {
        return mapper.load(object);
    }

    public List<T> queryIndexByKeyAndIntegerSk(T objectWithPk, String indexName, String skName, Integer skValue) {

        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withN(skValue.toString()));

        final DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.withHashKeyValues(objectWithPk);
        queryExpression.withRangeKeyCondition(skName, condition);
        queryExpression.withIndexName(indexName);
        queryExpression.withConsistentRead(false);
        queryExpression.withReturnConsumedCapacity(ReturnConsumedCapacity.INDEXES);

        var result =  mapper.query(this.parserType, queryExpression);
        return result;
    }

    public List<T> queryIndexByPk(T objectWithPk, String indexName) {
        final DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.withHashKeyValues(objectWithPk);
        queryExpression.withIndexName(indexName);
        queryExpression.withConsistentRead(false);
        queryExpression.withReturnConsumedCapacity(ReturnConsumedCapacity.INDEXES);

        var result = mapper.query(this.parserType, queryExpression);
        return result;
    }

    public List<T> scanIndex(String indexName) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withIndexName(indexName)
                .withReturnConsumedCapacity(ReturnConsumedCapacity.INDEXES);

        var result = mapper.scan(this.parserType, scanExpression);
        return result;
    }

    public List<T> queryPkWithSkPrefix(T objectWithPk, String skName, String skPrefix) {

        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                .withAttributeValueList(new AttributeValue(skPrefix));

        final DynamoDBQueryExpression<T> queryExpression = new DynamoDBQueryExpression<>();
        queryExpression.withHashKeyValues(objectWithPk);
        queryExpression.withRangeKeyCondition(skName, condition);
        queryExpression.withReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        var result = mapper.query(this.parserType, queryExpression);
        return result;
    }
}