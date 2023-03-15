package com.lucas.bank;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.staticInformation.StaticMessagingRouter;
import com.lucas.bank.shared.adapters.DistributedLock;
import com.lucas.bank.shared.sqs.SqsWrapper;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.shared.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@EnableWebMvc
public class SqsEventHandler {

    // ToDo - search a better way to handle multiple SQS entry points in the same file

    private final Logger log = LoggerFactory.getLogger(SqsEventHandler.class);
    private final DistributedLock distributedLock;
    private final SqsWrapper sqsWrapper;
    private final LoadLoanQuery loadLoanQuery;
    private final LoanTransactionUseCase loanTransactionUseCase;

    @RequestMapping(path = "/event-sqs", method = RequestMethod.POST)
    public SQSBatchResponse eventSqs(@RequestParam("type") String type, @RequestBody StreamLambdaHandler.AnnotatedSQSEvent message) {

        log.info("Received SQS event >> type: {}, body {}", type, new Gson().toJson(message));

        List<SQSBatchResponse.BatchItemFailure> batchItemFailures = new ArrayList<SQSBatchResponse.BatchItemFailure>();
        String messageId = "";
        String route = "";

        for (SQSEvent.SQSMessage event : message.getRecords()) {
            try {

                log.info("Attributes: {}", new Gson().toJson(event.getMessageAttributes()));

                messageId = event.getMessageId();

                log.info("Processing message id : " + messageId);

                if (!event.getMessageAttributes().containsKey(StaticMessagingRouter.ROUTER_KEY_NAME)) {
                    log.info("Ignoring message without router attribute");
                    continue;
                }

                route = event.getMessageAttributes().get(StaticMessagingRouter.ROUTER_KEY_NAME).getStringValue();

                if (route.equals(StaticMessagingRouter.ROUTE_DAILY_BATCH)) {
                    processDailyBatch();
                }

                if (route.equals(StaticMessagingRouter.ROUTE_LOAN_EXTRACTION)) {
                    DailyBatchDto batchDto = new Gson().fromJson(event.getBody(), DailyBatchDto.class);
                    processExtraction(batchDto.batchBlock);
                }

                if (route.equals(StaticMessagingRouter.ROUTE_LOAN_ACCRUAL)) {
                    AccrualDto accrualDto = new Gson().fromJson(event.getBody(), AccrualDto.class);
                    processAccrual(accrualDto.loanId);
                }

                log.info("Finish");

            } catch (Exception e) {
                log.error("Adding failure message to response, id: " + messageId);
                log.error("Exception executing route: " + route, e);
                batchItemFailures.add(new SQSBatchResponse.BatchItemFailure(messageId));
            }
        }

        return new SQSBatchResponse(batchItemFailures);
    }

    private void processDailyBatch() {
        log.info("Starting daily batch");
        for (int x = 1; x <= StaticInformation.BATCH_BLOCK_RANGE; x++) {
            var batchDto = new DailyBatchDto(x);
            sqsWrapper.sendMessage(StaticMessagingRouter.QUEUE_URL_LOAN_EXTRACTION(), StaticMessagingRouter.ROUTE_LOAN_EXTRACTION, batchDto);
            log.info("Published batch block: {}", x);
        }
    }

    private void processExtraction(Integer batchBlock) {
        log.info("Retrieving active loans for batch block: {}", batchBlock);
        var activeLoans = loadLoanQuery.listLoanByBatchBlock(batchBlock, "ACTIVE");

        log.info("Total active loans for batch block: {} - {}", batchBlock, activeLoans.size());

        for (Map.Entry<Long, String> activeLoan : activeLoans.entrySet()) {
            var accrualDto = new AccrualDto(activeLoan.getKey());
            sqsWrapper.sendMessage(StaticMessagingRouter.QUEUE_URL_LOAN_ACCRUAL(), StaticMessagingRouter.ROUTE_LOAN_ACCRUAL, accrualDto);
            log.info("Loan published to accrual, loan id: {}", accrualDto.loanId);
        }
    }

    private void processAccrual(Long loanId) {
        log.info("Processing loan accrual, id: {}", loanId);
        var accrualDate = DateTimeUtil.nowWithTimeZone();

        distributedLock.tryAcquire(loanId.toString());
        var transaction = UnitOfWork.newInstance();

        loanTransactionUseCase.dailyAccrual(loanId, accrualDate, transaction);

        transaction.commit();
        distributedLock.release();

        log.info("Loan account id: {} - accrual process completed", loanId);
    }

    class AccrualDto {
        public Long loanId;

        public AccrualDto(Long loanId) {
            this.loanId = loanId;
        }

        public Long getLoanId() {
            return loanId;
        }

        public void setLoanId(Long loanId) {
            this.loanId = loanId;
        }
    }

    class DailyBatchDto {

        public Integer batchBlock;

        public DailyBatchDto(Integer batchBlock) {
            this.batchBlock = batchBlock;
        }

        public Integer getBatchBlock() {
            return batchBlock;
        }

        public void setBatchBlock(Integer batchBlock) {
            this.batchBlock = batchBlock;
        }
    }
}