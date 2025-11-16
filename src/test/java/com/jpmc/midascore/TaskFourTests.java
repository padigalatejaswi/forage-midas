package com.jpmc.midascore;

import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
        properties = {
                // Keeps the port fix
                "spring.kafka.bootstrap-servers=localhost:9092",

                // 💡 CRITICAL FIX: Tells the client not to use modern version requests
                "spring.kafka.producer.properties.api.version.request=false",
                "spring.kafka.consumer.properties.api.version.request=false",

                // This is often needed for embedded brokers to ensure a compatible message format
                "spring.kafka.producer.properties.log.message.format.version=0.10.2.0"
        }
)
@DirtiesContext

public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    // ... (rest of the class is the same)

    @Autowired
    private KafkaProducer kafkaProducer; // Ignore IDE warning: Assigned by Spring

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired // 💡
    private UserRepository userRepository;

    // Inside TaskFourTests.java
// ...
    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();

        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Wait for Kafka + Incentive API to process
        Thread.sleep(5000);

        // ✅ Print Wilbur's balance
        var wilburBalance = userRepository.findByUserId("wilbur")
                .map(user -> user.getBalance())
                .orElse(null);

        System.out.println("----------------------------------------------------------");
        System.out.println("💰 WILBUR BALANCE = " + wilburBalance);
        System.out.println("----------------------------------------------------------");

        logger.info("✅ Test completed successfully.");
    }
}
