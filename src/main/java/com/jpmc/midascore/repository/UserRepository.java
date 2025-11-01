package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord; // 👈 Ensure you use the correct entity name (User or UserRecord)
import org.springframework.data.jpa.repository.JpaRepository; // 👈 CRITICAL: Use JpaRepository
import org.springframework.stereotype.Repository;

@Repository
// 🚨 CORRECT INTERFACE: JpaRepository and use String for the primary key (userId)
public interface UserRepository extends JpaRepository<UserRecord, String> {

    // NO NEED to define findById(String id) here.
    // It is inherited from JpaRepository and returns Optional<User>.
}