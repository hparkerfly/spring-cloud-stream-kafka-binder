# Spring Cloud Stream - Kafka Binder
## Description
This is an example of how `TimeWindows` work with `KStreams` binder. It focuses on how to use `KTable::suppress` so that no intermediate results are sent to the output topic until certain condition is met.

Besides, this project highlights how to configure both `retention.ms` and `segment.ms` times for `KStreams`.

This project uses:
* `Java 14`
* `SpringBoot 2.3.3`
* `Spring Cloud Hoxton.SR8`
* `Spring Cloud Stream Horsham.SR8`

## How it works
`KafkaProducerTest` sends a message to the `input` topic so that the aggregation begins. The aggregation algorithm itself is quite simple: it receives different String values which are concatenated. They are all grouped by the same key.

Thanks to `.suppress(Suppressed.untilWindowCloses(unbounded()))` no partial aggregations are sent to the `output` topic, but no final result is either.

Once the specified `retention.ms` and `segment.ms` times have been elapsed<sup>1</sup>, the messages belonging to the KStreams topics (KTable, changelog, repartition) are deleted.

<sup>1</sup> http://dalelane.co.uk/blog/?p=3993

## Requirements
Kafka broker running on port 9092
