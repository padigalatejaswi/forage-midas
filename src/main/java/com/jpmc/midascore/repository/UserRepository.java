package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserRecord, String> {

    // Required by TaskFourTests
    Optional<UserRecord> findByUserId(String userId);
}
