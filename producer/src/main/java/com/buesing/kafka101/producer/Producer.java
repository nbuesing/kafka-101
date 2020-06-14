package com.buesing.kafka101.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Producer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public void start(final Options options) {

        log.info("starting producer " + options);

    }

    private Map<String, Object> properties(final Options options) {
        final Map<String, Object> properties = new HashMap<>();

        return properties;
    }



    private static void pause(final long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (final InterruptedException e) {
            // ignore
        }
    }

    private static void dumpMetadata(final RecordMetadata metadata, final Exception exception) {
        if (exception != null) {
            log.error("unable to produce message, exception={}", exception.getMessage());
        } else if (metadata != null) {
            log.info("Callback:\n\ttopic     : {}\n\tpartition : {}\n\toffset    : {}\n\ttimestamp : {}\n\t", metadata.topic(), metadata.partition(), metadata.offset(), toDateString(metadata.timestamp()));
        }
    }

    private static String toDateString(final long epoch) {
        return FORMATTER.format(OffsetDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()));
    }

}
