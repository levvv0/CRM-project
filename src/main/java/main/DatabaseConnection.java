package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/crm-bd";
    private static final String USER = "postgres";

    public static Connection getConnection() throws SQLException {
        String password = System.getenv("CRM_DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new SQLException("Environment variable CRM_DB_PASSWORD is not set");
        }

        return DriverManager.getConnection(URL, USER, password);
    }
}

