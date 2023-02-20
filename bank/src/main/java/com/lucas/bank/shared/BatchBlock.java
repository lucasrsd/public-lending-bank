package com.lucas.bank.shared;

import java.util.concurrent.ThreadLocalRandom;

public class BatchBlock {
    public static Integer next() {
        var size = StaticInformation.BATCH_BLOCK_SIZE;

        return ThreadLocalRandom.current().nextInt(1, size);
    }
}
