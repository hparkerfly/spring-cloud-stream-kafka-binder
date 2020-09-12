package example;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface Bindings {

  @Input("input")
  KStream<String, String> sendInfo();

  @Output("output")
  KStream<String, String> aggregatedInfo();
}
