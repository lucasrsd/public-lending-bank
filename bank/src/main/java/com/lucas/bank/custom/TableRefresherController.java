package com.lucas.bank.custom;

import com.lucas.bank.shared.adapters.WebAdapter;
import com.lucas.bank.shared.dynamoDb.DynamoDbTableRefresherWrapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/table-refresher")
public class TableRefresherController {
    private final Logger log = LoggerFactory.getLogger(TableRefresherController.class);

    private final DynamoDbTableRefresherWrapper dynamoDbTableRefresherWrapper;


    @PostMapping(path = "/{version}")
    void create(@NotNull @PathVariable("version") Long version) {
        log.info("Starting full table refresh");

        var aws = System.getenv("POWERTOOLS_SERVICE_NAME");

        if (aws != null){
            throw new RuntimeException("Long time execution disabled due to the lambda timeout, please run this process in a different place ( Like your machine :D ) ");
        }

        dynamoDbTableRefresherWrapper.refreshTableVersion(version);
        log.info("Done");
    }
}
