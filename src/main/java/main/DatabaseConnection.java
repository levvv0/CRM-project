package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/crm_db";
    private static final String DEFAULT_USER = "postgres";

    public static Connection getConnection() throws SQLException {
        String url = DatabaseConfig.getConfigValue("CRM_DB_URL", DEFAULT_URL);
        String user = DatabaseConfig.getConfigValue("CRM_DB_USER", DEFAULT_USER);
        String password = DatabaseConfig.getConfigValue("CRM_DB_PASSWORD", null);

        if (password == null || password.isBlank()) {
            throw new SQLException("CRM_DB_PASSWORD is not set");
        }

        return DriverManager.getConnection(url, user, password);
    }
}