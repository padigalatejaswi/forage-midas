package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal; // 👈 NEW IMPORT

@Component
public class UserPopulator {
    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    public void populate() {
        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");
        for (String userLine : userLines) {
            String[] userData = userLine.split(", ");

            // 🚨 CORRECTED LINE: Convert string directly to BigDecimal for precision
            BigDecimal balance = new BigDecimal(userData[1].trim());

            // Note: Use 'UserRecord' or 'User' based on your entity name.
            UserRecord user = new UserRecord(userData[0].trim(), balance);
            databaseConduit.save(user);
        }
    }
}