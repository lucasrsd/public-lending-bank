package com.lucas.bank;

import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.gson.Gson;
import com.lucas.bank.shared.sqs.SqsWrapper;
import com.lucas.bank.shared.staticInformation.StaticMessagingRouter;
import com.lucas.bank.shared.stream.StreamEventDTO;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.time.Instant;
import java.util.*;


@RestController
@RequiredArgsConstructor
@EnableWebMvc
public class DynamoEventHandler {

    private final Logger log = LoggerFactory.getLogger(DynamoEventHandler.class);
    private final SqsWrapper sqsWrapper;

    @RequestMapping(path = "/event-dynamo", method = RequestMethod.POST)
    public void eventDynamo(@RequestParam("type") String type, @RequestBody Object input) throws JsonProcessingException {

        log.info("Received Dynamo event >> type: {}, body {}", type, new Gson().toJson(input));

        var dynamoEvents = mapToDynamoEvent(input);

        if (dynamoEvents == null) {
            log.info("Dynamo stream event is null, skipping");
            return;
        }

        for (Record record : dynamoEvents) {
            publishToQueue(record);
        }
    }

    private void publishToQueue(Record record) {

        try {
            List<Item> listOfItem = new ArrayList<>();
            List<Map<String, AttributeValue>> listOfMaps = null;

            if ("INSERT".equals(record.getEventName()) || "MODIFY".equals(record.getEventName())) {
                listOfMaps = new ArrayList<Map<String, AttributeValue>>();
                listOfMaps.add(record.getDynamodb().getNewImage());
                listOfItem = InternalUtils.toItemList(listOfMaps);
            } else {
                log.warn("Deleting event type");
            }

            log.info(new Gson().toJson(listOfItem));

            Gson gson = new Gson();
            Item item = listOfItem.get(0);

            String json = gson.toJson(item.asMap());

            var pk = record.getDynamodb().getKeys().get("pk").getS();
            var sk = "";

            if (record.getDynamodb().getKeys().containsKey("sk")) {
                sk = record.getDynamodb().getKeys().get("sk").getS();
            }

            var message = StreamEventDTO
                    .builder()
                    .operation(record.getEventName())
                    .keys(Map.of("pk", pk,
                            "sk", sk))
                    .jsonData(json);

            sqsWrapper.sendMessage(StaticMessagingRouter.QUEUE_URL_BANK_SQL_PROJECTOR(), StaticMessagingRouter.ROUTE_BANK_SQL_PROJECTOR, message);

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class RecordsSerializable {
        public List<Record> Records;
    }

    private List<Record> mapToDynamoEvent(Object object) throws JsonProcessingException {
        ObjectMapper mapper = LambdaContainerHandler.getObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.registerModule(new JodaModule());
        mapper.addMixIn(Record.class, RecordIgnoreDuplicateMethods.class);
        mapper.addMixIn(StreamRecord.class, StreamRecordIgnoreDuplicateMethods.class);
        mapper.setPropertyNamingStrategy(new PropertyNamingFix());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new TimestampDeserializer());
        mapper.registerModule(module);

        RecordsSerializable records = mapper.readValue(new Gson().toJson(object), RecordsSerializable.class);

        log.info("Parsed event: {}", new Gson().toJson(records));

        if (records == null) {
            log.info("Event null");
            return null;
        }

        if (records.Records == null) {
            log.info("Records null");
            return null;
        }

        for (Record rec : records.Records) {
            log.info("Record: {}", new Gson().toJson(rec));
        }

        return records.Records;
    }

    interface RecordIgnoreDuplicateMethods {
        @JsonIgnore
        void setEventName(OperationType eventName);

        @JsonProperty("eventName")
        void setEventName(String eventName);
    }

    interface StreamRecordIgnoreDuplicateMethods {
        @JsonIgnore
        void setStreamViewType(StreamViewType streamViewType);

        @JsonProperty("StreamViewType")
        void setStreamViewType(String streamViewType);
    }

    public static class PropertyNamingFix extends PropertyNamingStrategy.PropertyNamingStrategyBase {
        @Override
        public String translate(String propertyName) {
            switch (propertyName) {
                case "eventID":
                    return "eventID";
                case "eventVersion":
                    return "eventVersion";
                case "eventSource":
                    return "eventSource";
                case "awsRegion":
                    return "awsRegion";
                case "dynamodb":
                    return "dynamodb";
                case "eventSourceARN":
                    return "eventSourceARN";
                default:
                    String first = propertyName.substring(0, 1);
                    String rest = propertyName.substring(1);
                    return first.toUpperCase() + rest;
            }
        }
    }

    public class TimestampDeserializer extends StdDeserializer<Date> {
        public TimestampDeserializer() {
            this(null);
        }

        public TimestampDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return Date.from(Instant.ofEpochSecond(jp.getValueAsLong()));
        }
    }
}