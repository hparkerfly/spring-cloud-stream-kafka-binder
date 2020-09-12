package example;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
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

    var window = Duration.ofSeconds(10L);
    var grace = Duration.ofSeconds(1L);
    var retention = Duration.ofSeconds(60L);

    return input
      .groupBy((k, v) -> "explicitKey")
      .windowedBy(TimeWindows.of(window).grace(grace))
      .aggregate(
        () -> "",
        (aggKey, actual, accumulated) -> String.join(" ", accumulated, actual),
        Named.as("aggregation"),
        Materialized.<String, String, WindowStore<Bytes, byte[]>>as("InfoStore")
          .withRetention(retention))
      .suppress(Suppressed.untilWindowCloses(unbounded()).withName("InfoStoreSuppressed"))
      .toStream()
      .map((k, v) -> pair(k.key() + "-" + k.window().toString(), v));
  }
}
