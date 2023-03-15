package com.lucas.bank.shared;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import com.lucas.bank.shared.adapters.DistributedLock;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.shared.util.ErrorDetails;
import com.lucas.bank.shared.util.ErrorItem;
import com.lucas.bank.shared.util.ErrorListDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @Autowired
    private DistributedLock distributedLock;

    private final Logger LOG = LoggerFactory.getLogger(ControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception exception, WebRequest request) {
        LOG.error("Response Status Exception", exception);
        LOG.error("Request Description: {}", request.getDescription(false));

        distributedLock.release();

        return new ResponseEntity<>(new ErrorDetails(DateTimeUtil.nowWithTimeZone(), exception.toString(), request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> bindExceptionHandler(BindException exception, WebRequest request) {
        LOG.error("Bind Exception", exception);
        LOG.error("Request Description: {}", request.getDescription(false));

        var inputParameters = exception.getBindingResult().getTarget();

        LOG.error("Request parameters: {}", new Gson().toJson(inputParameters));

        List<ErrorItem> errors = new ArrayList<>();

        for(ObjectError error : exception.getBindingResult().getAllErrors()){
            errors.add(new ErrorItem(error.getObjectName(), error.getCode(), error.getDefaultMessage()));
        }

        LOG.error("Response errors: {}", new Gson().toJson(errors));


        distributedLock.release();
        return new ResponseEntity<>(new ErrorListDetails(DateTimeUtil.nowWithTimeZone(), errors, request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException exception, WebRequest request) {
        LOG.error("Illegal argument Exception", exception);
        LOG.error("Request Description: {}", request.getDescription(false));

        distributedLock.release();
        return new ResponseEntity<>(new ErrorDetails(DateTimeUtil.nowWithTimeZone(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException exception, WebRequest request) {
        LOG.error("Response Status Exception", exception);
        LOG.error("Request Description: {}", request.getDescription(false));

        distributedLock.release();
        return new ResponseEntity<>(new ErrorDetails(DateTimeUtil.nowWithTimeZone(), exception.getReason(), request.getDescription(false)), exception.getStatus());
    }
}