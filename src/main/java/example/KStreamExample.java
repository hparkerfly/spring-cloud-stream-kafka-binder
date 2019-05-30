package example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.state.SessionStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.time.Duration;

import static org.apache.kafka.streams.KeyValue.pair;
import static org.apache.kafka.streams.kstream.Suppressed.BufferConfig.unbounded;

@EnableBinding(Bindings.class)
public class KStreamExample {

  @StreamListener("input")
  @SendTo("output")
  public KStream<String, String> aggregateInfo(KStream<String, String> input) {

    Duration window = Duration.ofSeconds(5L);

    return input
      .groupBy((k, v) -> "explicitKey")
      .windowedBy(SessionWindows.with(window).grace(window))
      .aggregate(
        () -> "",
        (aggKey, newValue, valueAggregate) -> String.join(" ", valueAggregate, newValue),
        (aggKey, leftList, rightList) -> String.join("", leftList, rightList),
        Materialized.<String, String, SessionStore<Bytes, byte[]>>as("InfoStore")
          .withKeySerde(Serdes.String())
          .withValueSerde(Serdes.String()))
      .suppress(Suppressed.untilWindowCloses(unbounded()))
      .toStream()
      .map((key, value) -> pair(key.key() + "--" + key.window().toString(), value));
  }
}
