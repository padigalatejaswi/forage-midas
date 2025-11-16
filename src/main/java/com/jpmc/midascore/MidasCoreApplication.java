package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.annotation.PostConstruct; // or javax.annotation.PostConstruct if needed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MidasCoreApplication {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }

    // 🔹 This runs automatically after Spring loads everything
    @PostConstruct
    public void printLoadedUsers() {
        System.out.println("--------------------------------------------------");
        System.out.println("DEBUG: Loaded users from UserRepository:");
        for (UserRecord user : userRepository.findAll()) {
            System.out.println("UserId=" + user.getUserId() + " | Balance=" + user.getBalance());
        }
        System.out.println("--------------------------------------------------");
    }
}
