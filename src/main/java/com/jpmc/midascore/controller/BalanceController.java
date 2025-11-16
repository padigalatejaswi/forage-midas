package com.jpmc.midascore.controller;

import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jpmc.midascore.Balance;


@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") String userId) {

        return userRepository.findById(userId)
                .map(user -> new Balance(user.getBalance().doubleValue()))
                .orElse(new Balance(0.0));
    }
}
