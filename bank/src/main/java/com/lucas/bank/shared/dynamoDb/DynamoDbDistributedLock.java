package com.lucas.bank.shared.dynamoDb;

import com.amazonaws.services.dynamodbv2.AcquireLockOptions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBLockClientOptions;
import com.amazonaws.services.dynamodbv2.LockItem;
import com.lucas.bank.shared.StaticInformation;
import com.lucas.bank.shared.adapters.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

// ToDo - create generic error handling to release current ongoing locks
// ToDo - Inject all classes, avoid new()
@Component
public class DynamoDbDistributedLock implements DistributedLock {

    private final Logger LOG = LoggerFactory.getLogger(DynamoDbDistributedLock.class);
    private DynamoDbClient client;
    private AmazonDynamoDBLockClient lockClient;
    private LockItem lockedItem;

    public DynamoDbDistributedLock() {
        this.client =  DynamoDbClient.builder().region(Region.of(StaticInformation.AWS_REGION_STRING)).build();
        this.lockClient = new AmazonDynamoDBLockClient(
                AmazonDynamoDBLockClientOptions.builder(this.client, StaticInformation.LOCK_TABLE_NAME)
                        .withTimeUnit(TimeUnit.SECONDS)
                        .withLeaseDuration(5L) // ToDo - review lock lease duration
                        .withHeartbeatPeriod(1L) // ToDo - review lock lease duration
                        .withCreateHeartbeatBackgroundThread(true)
                        .build());
    }

    @Override
    public LockItem tryAcquire(String key) {
        LOG.info("Trying to acquire lock for partition key: {}", key);
        final Optional<LockItem> lockItem;
        try {
            lockItem = this.lockClient.tryAcquireLock(AcquireLockOptions.builder(key).build());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (lockItem.isPresent()) {
            LOG.info("Lock acquired for partition key: {}", key);
            lockedItem = lockItem.get();
            return lockedItem;
        } else {
            LOG.error("Fail to acquire lock for partition key: {}", key);
            throw new RuntimeException("Fail to acquire lock");
        }
    }

    @Override
    public void release() {

        if (lockedItem == null){
            LOG.info("Locked item null");
            return;
        }
        LOG.info("Releasing lock for partition key: {}", lockedItem.getPartitionKey());
        this.lockClient.releaseLock(lockedItem);
    }
}