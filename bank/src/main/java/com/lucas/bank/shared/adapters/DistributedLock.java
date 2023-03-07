package com.lucas.bank.shared.adapters;

import com.amazonaws.services.dynamodbv2.LockItem;

public interface DistributedLock {
    LockItem tryAcquire(String key);
    void release();
}
