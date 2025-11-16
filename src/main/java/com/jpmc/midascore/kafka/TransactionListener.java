package com.jpmc.midascore.kafka;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.model.Incentive;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.IncentiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    private final UserRepository userRepository;
    private final IncentiveService incentiveService;

    @Autowired
    public TransactionListener(UserRepository userRepository,
                               IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.incentiveService = incentiveService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    @Transactional
    public void listen(Transaction transaction) {
        logger.info("Processing transaction: {}", transaction);

        // Mapping numeric IDs to usernames
        Map<Long, String> idToUser = Map.of(
                1L, "waldorf",
                2L, "wilbur",
                3L, "xavier",
                4L, "yvette",
                5L, "zachary",
                6L, "victor",
                7L, "ursula",
                8L, "tina",
                9L, "steven"
        );

        String senderName = idToUser.get(transaction.getSenderId());
        String recipientName = idToUser.get(transaction.getRecipientId());

        if (senderName == null || recipientName == null) {
            logger.warn("Unknown sender/recipient mapping for transaction: {}", transaction);
            return;
        }

        // Lookup by username (String ID)
        Optional<UserRecord> senderOpt = userRepository.findByUserId(senderName);
        Optional<UserRecord> recipientOpt = userRepository.findByUserId(recipientName);


        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            logger.warn("Sender or recipient not found for transaction: {}", transaction);
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());

        if (sender.getBalance().compareTo(amount) < 0) {
            logger.warn("Insufficient funds for sender {}. Transaction discarded.", senderName);
            return;
        }

        Incentive incentive = incentiveService.getIncentiveFor(transaction);
        BigDecimal incentiveAmount = (incentive != null)
                ? BigDecimal.valueOf(incentive.getAmount())
                : BigDecimal.ZERO;

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount).add(incentiveAmount));

        userRepository.save(sender);
        userRepository.save(recipient);

        logger.info("Transaction processed successfully: sender={}, recipient={}, amount={}, incentive={}",
                senderName, recipientName, amount, incentiveAmount);

        if ("wilbur".equalsIgnoreCase(recipient.getUserId())) {
            System.out.println("******** WILBUR FINAL BALANCE: " + recipient.getBalance() + " ********");
        }
    }
}
