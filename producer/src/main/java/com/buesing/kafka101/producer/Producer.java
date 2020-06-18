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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Producer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public void start(final Options options) {

        log.info("starting producer " + options);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties(options));

        for (int i = 0; i < options.getCount(); i++) {
            Future<RecordMetadata> f = producer.send(new ProducerRecord<>(options.getTopic(), "KEY_" + i, "VALUE_" + i), (metadata, exception) -> {
                dumpMetadata(metadata, exception);
            });
        }

//        try {
//            TimeUnit.SECONDS.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        producer.close();
    }

    private Map<String, Object> properties(final Options options) {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, options.getBootstrapServers());
        properties.put(ProducerConfig.SECURITY_PROVIDERS_CONFIG, "PLAINTEXT");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.LINGER_MS_CONFIG, 10L);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);

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
