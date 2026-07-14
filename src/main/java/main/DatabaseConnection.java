package main;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/crm_db";
    private static final String DEFAULT_USER = "postgres";
    private static HikariDataSource dataSource;

    public static HikariDataSource createDataSource() throws SQLException {
        String url = DatabaseConfig.getConfigValue("CRM_DB_URL", DEFAULT_URL);
        String user = DatabaseConfig.getConfigValue("CRM_DB_USER", DEFAULT_USER);
        String password = DatabaseConfig.getConfigValue("CRM_DB_PASSWORD", null);

        if (password == null || password.isBlank()) {
            throw new SQLException("CRM_DB_PASSWORD is not set");
        }

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(5);
        config.setPoolName("CRM-HikariPool");

        return new HikariDataSource(config);
    }

    private static HikariDataSource getDataSource() throws SQLException{

        if(dataSource == null || dataSource.isClosed()){
            dataSource = createDataSource();
        }

        return dataSource;
    }

    public static Connection getConnection() throws SQLException{

        return getDataSource().getConnection();
    }

    public static void closePool(){

        if(dataSource != null && !dataSource.isClosed()){
            dataSource.close();
        }
    }
}