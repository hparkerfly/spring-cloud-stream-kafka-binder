# Spring Cloud Stream - Kafka Binder
This is an example on how session windows work with kstream. It focuses on how to use suppress() so that no intermediate results are sent to the output topic.

`KafkaProducerTest` sends a message to the `input` topic so that the aggregation can begin. The aggregation process itself is simple: it receives different String values which get concatenated. 

Thanks to `.suppress(Suppressed.untilWindowCloses(unbounded()))` no partial aggregations are sent to the `output` topic, but no final aggregation is sent either.


## Requirements
Kafka running on port 9092
