package com.jpmc.midascore;

import com.jpmc.midascore.repository.UserRepository; // 👈 NEW IMPORT
import com.jpmc.midascore.entity.UserRecord; // 👈 NEW IMPORT
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional; // 👈 NEW IMPORT

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired // 👈 NEW: Inject the repository to access user data
    private UserRepository userRepository;

    @Test
    void task_three_verifier() throws InterruptedException {
        // 1. Setup and Transaction Sending
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Give time for Kafka listener (TransactionListener) to process all messages
        // The instructions suggest Thread.sleep(2000), but we'll use a slightly longer one
        // to ensure all asynchronous Kafka messages are processed and database updates are complete.
        Thread.sleep(5000);

        // 2. Query the required user and LOG the final balance
        Optional<UserRecord> waldorfUserOptional = userRepository.findById("waldorf");

        if (waldorfUserOptional.isPresent()) {
            UserRecord waldorf = waldorfUserOptional.get();
            // 👈 Set your breakpoint on the next line!
            logger.info("FINAL BALANCE OF WALDORF: {}", waldorf.getBalance());
        } else {
            logger.error("Waldorf user not found in the database!");
        }

        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to find out what waldorf's balance is after all transactions are processed");
        logger.info("kill this test once you find the answer");

        // 3. Keep the test running so you can inspect the database/logs
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}