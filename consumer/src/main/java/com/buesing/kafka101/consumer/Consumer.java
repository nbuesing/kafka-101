package com.buesing.kafka101.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Consumer {

    public void start(final Options options) {

        log.info("starting consumer");

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties(options));

        consumer.subscribe(Collections.singleton(options.getTopic()), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                log.debug("revoked : " + collection.toString());
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                log.debug("assigned : " + collection.toString());
            }
        });

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500L));
            records.iterator().forEachRemaining((record) -> {
                dumpRecord(record);
            });
            //consumer.commitSync();
        }
    }

    private Map<String, Object> properties(final Options options) {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, options.getBootstrapServers());
        properties.put(ConsumerConfig.SECURITY_PROVIDERS_CONFIG, "PLAINTEXT");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "sample.consumer.2");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        return properties;
    }

    private static void dumpRecord(final ConsumerRecord<String, String> record) {
        log.info("Record:\n\ttopic     : {}\n\tpartition : {}\n\toffset    : {}\n\tkey       : {}\n\tvalue     : {}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }

}
