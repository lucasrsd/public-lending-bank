package com.lucas.bank.shared;

import com.lucas.bank.shared.adapters.DistributedLock;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.shared.util.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerAdvice {

    @Autowired
    private DistributedLock distributedLock;

    private final Logger LOG = LoggerFactory.getLogger(PersistenceTransactionManager.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception exception, WebRequest request) {
        LOG.error("{} - Exception: {}", new Date(), exception.toString());
        LOG.error("{} - Request Description: {}", new Date(), request.getDescription(false));

        distributedLock.release();
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.toString(), request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
