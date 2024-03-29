package com.lucas.bank;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.ApiGatewayRequestIdentity;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyRequestContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.Headers;
import com.amazonaws.serverless.proxy.model.MultiValuedTreeMap;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.dynamodbv2.model.OperationType;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.dynamodbv2.model.StreamViewType;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.lucas.bank.shared.util.DateTimeUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Link: https://github.com/huksley/serverless-java-spring-boot
// Link dynamo: https://www.diversit.eu/post/2017/11/01/deserializing-aws-dynamo-event.html

public class StreamLambdaHandler implements RequestStreamHandler {
    private static Logger log = LoggerFactory.getLogger(StreamLambdaHandler.class);

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            log.info("Starting AWS Lambda");
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(BankApplication.class);
            log.info("Created AWS Handler, {}", handler);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        log.info("Handle request {}, {}, {}, {}", context, context.getFunctionName(), context.getInvokedFunctionArn(),
                context.getLogStreamName());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bos);
        String event = new String(bos.toByteArray(),
                LambdaContainerHandler.getContainerConfig().getDefaultContentCharset());
        log.info("Got event {} context {}", event);

        ObjectMapper mapper = LambdaContainerHandler.getObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.registerModule(new JodaModule());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(SNSRecord.class, new SNSRecordDeserializer());
        module.addDeserializer(SQSMessage.class, new SQSMessageDeserializer());
        mapper.registerModule(module);

        AwsProxyRequest request = mapper.readValue(event, AwsProxyRequest.class);
        if (request.getHttpMethod() == null || "".equals(request.getHttpMethod())) {
            log.info("Parsing AWS event {}", event);
            Map<String, Object> raw = (Map<String, Object>) mapper.readValue(event, Map.class);

            // Peek inside one event
            if (raw.get("Records") instanceof List) {
                List<Map<String, Object>> events = (List<Map<String, Object>>) raw.get("Records");
                if (events.size() > 0) {
                    raw = events.get(0);
                } else {
                    log.warn("Empty, dummy event records {}", events);
                }
            }

            String eventSource = (String) raw.get("source"); // CloudWatch event
            if (eventSource == null) {
                eventSource = (String) raw.get("EventSource"); // SNS
            }
            if (eventSource == null) {
                eventSource = (String) raw.get("eventSource"); // SQS!?!
            }

            if (eventSource == null) {
                log.warn("Can`t get event type: {}", raw);
            } else if ("aws.events".equals(eventSource)) {
                ScheduledEvent ev = mapper.readValue(event, AnnotatedScheduledEvent.class);
                request = convertToRequest(ev, event, AnnotatedScheduledEvent.class.getName(), context, "scheduled");
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            } else if ("aws:sns".equals(eventSource)) {
                SNSEvent ev = mapper.readValue(event, AnnotatedSNSEvent.class);
                request = convertToRequest(ev, event, AnnotatedSNSEvent.class.getName(), context, "sns");
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            } else if ("aws:sqs".equals(eventSource)) {
                SQSEvent ev = mapper.readValue(event, AnnotatedSQSEvent.class);
                request = convertToRequest(ev, event, AnnotatedSQSEvent.class.getName(), context, "sqs");
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            }
            else if ("aws:dynamodb".equals(eventSource)) {
                request = convertToRequest(raw, event, DynamodbEvent.class.getName(), context, "dynamo");
                log.info("Converted to {}", request);
                AwsProxyResponse response = handler.proxy(request, context);
                mapper.writeValue(outputStream, response);
            } else {
                log.warn("Unhandled event type {}", eventSource);
            }
        } else {
            log.info("Handling via Spring Boot rest APIs {}", request);
            AwsProxyResponse response = handler.proxy(request, context);
            mapper.writeValue(outputStream, response);
        }
    }

    // Schedule

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedScheduledEvent extends ScheduledEvent {

    }

    // SNS

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSNSRecord extends SNSRecord {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSNSEvent extends SNSEvent {
        @Override
        @JsonProperty("Records")
        public List<SNSRecord> getRecords() {
            return super.getRecords();
        }
    }

    public class SNSRecordDeserializer extends StdDeserializer<SNSRecord> {
        public SNSRecordDeserializer() {
            this(null);
        }

        public SNSRecordDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public SNSRecord deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return ctxt.readValue(jp, AnnotatedSNSRecord.class);
        }
    }

    // SQS
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSQSMessage extends SQSMessage {
    }

    public class SQSMessageDeserializer extends StdDeserializer<SQSMessage> {
        public SQSMessageDeserializer() {
            this(null);
        }

        public SQSMessageDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public SQSMessage deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return ctxt.readValue(jp, AnnotatedSQSMessage.class);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnnotatedSQSEvent extends SQSEvent {
        @Override
        @JsonProperty("Records")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public List<SQSMessage> getRecords() {
            return super.getRecords();
        }
    }

    public static AwsProxyRequest convertToRequest(Object ev, String json, String className, Context context, String route) {
        AwsProxyRequest r = new AwsProxyRequest();
        r.setPath("/event-" + route);
        r.setResource("/event-" + route);
        r.setHttpMethod("POST");
        MultiValuedTreeMap<String, String> q = new MultiValuedTreeMap<String, String>();
        q.putSingle("type", className);
        r.setBody(json);
        r.setMultiValueQueryStringParameters(q);
        Headers h = new Headers();
        h.putSingle("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        r.setMultiValueHeaders(h);
        AwsProxyRequestContext rc = new AwsProxyRequestContext();
        rc.setIdentity(new ApiGatewayRequestIdentity());
        r.setRequestContext(rc);
        return r;
    }
}