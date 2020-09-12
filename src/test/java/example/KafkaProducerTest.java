package example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = KafkaProducerConfig.class)
@RunWith(SpringRunner.class)
public class KafkaProducerTest {

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  public void sendMessage() {
    Stream.of("this", "is", "a", "test").forEachOrdered(value -> kafkaTemplate.send("kstream-input", value));
  }
}