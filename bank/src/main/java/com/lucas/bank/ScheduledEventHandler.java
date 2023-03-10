package com.lucas.bank;

import com.lucas.bank.shared.staticInformation.StaticMessagingRouter;
import com.lucas.bank.shared.sqs.SqsWrapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequiredArgsConstructor
@EnableWebMvc
public class ScheduledEventHandler {

    private final Logger log = LoggerFactory.getLogger(ScheduledEventHandler.class);
    private final SqsWrapper sqsWrapper;
    private final String RESOURCE_DAILY_BATCH = "/DailySchedule";

    @RequestMapping(path = "/event-scheduled", method = RequestMethod.POST)
    public void eventScheduled(@RequestParam("type") String type, @RequestBody StreamLambdaHandler.AnnotatedScheduledEvent message) {

        log.info("Received Scheduled event, type: {}", type);

        if (message.getResources().stream().anyMatch(r -> r.endsWith(RESOURCE_DAILY_BATCH))) {
            sqsWrapper.sendMessage(StaticMessagingRouter.QUEUE_URL_DAILY_BATCH(), StaticMessagingRouter.ROUTE_DAILY_BATCH, null);
        } else {
            log.error("Incompatible resource, message ignored");
            return;
        }

        log.info("Scheduled event processed.");
    }
}
