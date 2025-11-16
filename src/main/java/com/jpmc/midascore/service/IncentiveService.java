package com.jpmc.midascore.service;

import com.jpmc.midascore.kafka.Transaction;
import com.jpmc.midascore.model.Incentive;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    private static final String INCENTIVE_URL = "http://localhost:8080/incentive";
    private final RestTemplate restTemplate = new RestTemplate();

    public Incentive getIncentiveFor(Transaction transaction) {
        return restTemplate.postForObject(INCENTIVE_URL, transaction, Incentive.class);
    }
}
