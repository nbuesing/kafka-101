

# Kafka 101

A project to provide plumbing of an Apache Kafka Cluster and empty projects for writing your own producer and consumer

## Cluster

The provided Apache Kafka Cluster is a 3 Broker / 1 Zookeeper cluster through the use of Confluent's Docker Images.  In addition to the core cluster,  a schema registry and monitoring tools are also provided.

* Starting the Apache Kafka Cluster

    `./gradlew cluster:composeUp` or `docker-compose up -d`

* Stopping the Apache Kafka Cluster

    `./gradlew cluster:composeDown` or `docker-compose down`
    
* Monitoring Cluster through Grafana 

    `./gradlew cluster:grafana` or `http://localhost:3000`

    * Dashboard
     
     Shows Health of Cluster
        
        [Dashboard](./doc/grafana_dashboard.png)

    * Topic  
    
    Shows Activity of a given Topic
       
* Kafka Visualization through Web Based Tool

    `./gradlew cluster:akhq` or `http://localhost:8080`

  If port 8080 conflicts with other services you have running on your machine, changing port mapping, e.g. "9091:8080" works.
     
## Topic    
    
* Create Topic

   ```
   kafka-topics \
     --bootstrap-server localhost:19092 \
     --replication-factor 3 \
     --partitions 10 \
     --create \
     --topic isr2
   ```

* Create Topic with different settings

   ```
   kafka-topics \
     --bootstrap-server localhost:19092 \
     --replication-factor 3 \
     --partitions 10 \
     --create \
     --config min.insync.replicas=3 \
     --topic isr3
   ```
  
* Producer

```
kafka-console-producer --bootstrap-server localhost:19092 --property parse.key=true --property key.separator=\| --topic foo
```
  
* Consumer

```
kafka-console-consumer --bootstrap-server localhost:19092 --from-beginning --property print.key=true --property key.separator=" | "  --topic foo
```
    
 
   
## Useful command line options to produce and consume string based messages to a given topic

  * Produce
  
    ```
    kafka-console-producer \
        --broker-list localhost:19092 \
        --property parse.key=true \
        --property key.separator=\| \
        --producer-property acks=-1 \
        --topic ${topic}
    ```

  * Consume
  
    ```
    kafka-console-consumer \
        --bootstrap-server localhost:19092 \
        --property print.key=true \
        --property key.separator=\| \
        --from-beginning \
        --topic ${topic}
    ```

## Avro

  * This is out of scope for current provided content.
  
### Useful command line options to produce and consume Avro based messages to a given topic

  * Produce
  
  Producing is tricky, in that you have to provide the Avro Schema and you must provide message in a JSON format
  that the command line tool can properly convert into Avro.  Here is an example as a bourne shell script.

        ```
        #!/bin/sh
        
        value_schema=$(cat <<EOF
        {
          "type": "record",
          "name": "IdAndName",
          "namespace": "com.buesing",
          "fields": [
            {
              "name": "id",
              "type": "string"
            },
            {
              "name": "name",
              "type": "string"
            }
          ]
        }
        EOF
        )
        
        PAYLOAD=$(cat <<EOF
        ABC|{"id" : "ABC", "name" : "John Doe"}
        EOF
        )
        
        echo $PAYLOAD |
        kafka-avro-console-producer \
            --broker-list localhost:19092 \
            --property schema.registry.url="http://localhost:8081" \
            --property key.serializer=org.apache.kafka.common.serialization.StringSerializer \
            --property value.schema="${value_schema}" \
            --property parse.key=true \
            --property key.separator=\| \
            --topic $1 
    ```

   * Consume
       
    ```
    kafka-avro-console-consumer \
        --bootstrap-server localhost:19092 \
        --from-beginning \
        --topic ${topic} \
        --property print.key=true \
        --property key.separator=\| \
        --key-deserializer=org.apache.kafka.common.serialization.BytesDeserializer
    ```
    