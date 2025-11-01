package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class UserRecord {

    @Id //
    private String userId;

    private BigDecimal balance;

    // Constructors
    public UserRecord() {}

    public UserRecord(String userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    // Getters and Setters (Necessary for JPA and business logic)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}