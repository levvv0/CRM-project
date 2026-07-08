package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/crm-bd";
    private static final String DEFAULT_USER = "postgres";

    public static Connection getConnection() throws SQLException {
        String url = getConfigValue("CRM_DB_URL", DEFAULT_URL);
        String user = getConfigValue("CRM_DB_USER", DEFAULT_USER);
        String password = getConfigValue("CRM_DB_PASSWORD", null);

        if (password == null || password.isBlank()) {
            throw new SQLException("CRM_DB_PASSWORD is not set");
        }

        return DriverManager.getConnection(url, user, password);
    }

    private static String getConfigValue(String name, String defaultValue) {
        String propertyValue = System.getProperty(name);

        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String envValue = System.getenv(name);

        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return defaultValue;
    }
}