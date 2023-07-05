package com.package1;

import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NumberController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fetchapi";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "@messi89";

    @PostMapping("/fetchNumber")
    public NumberResponse fetchNextNumber(@RequestBody CategoryRequest request) throws InterruptedException {
        int categoryCode = request.getCategoryCode();
        String requestId = getRequestId();
        synchronized (requestId) {
            long fetchedValue = fetchValueFromTable(categoryCode);
            long nextNumber = calculateNextNumber(fetchedValue);
            updateTable(categoryCode, nextNumber);
            Thread.sleep(5000); // Introduce a 5-second delay to simulate processing

            return new NumberResponse(fetchedValue, nextNumber);
        }
    }
    public String getRequestId(){
        String s = UUID.randomUUID().toString();
        return s;
    }

    private int fetchValueFromTable(int categoryCode) {
        try
                (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT value FROM data WHERE CategoryCode = ? ")) {
            statement.setInt(1, categoryCode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private long calculateNextNumber(long fetchedValue) {
        long nextNumber = fetchedValue + 1;
        while (!sumOfDigitsEqualsOne(nextNumber)) {
            nextNumber++;
        }
        return nextNumber;
    }

    private boolean sumOfDigitsEqualsOne(long number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum==1;
    }

    private void updateTable(int categoryCode, long nextNumber) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("UPDATE data SET value = ? WHERE CategoryCode = ?")) {
            statement.setLong(1, nextNumber);
            statement.setInt(2, categoryCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}