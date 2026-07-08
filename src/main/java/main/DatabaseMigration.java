package main;
import org.flywaydb.core.Flyway;

public class DatabaseMigration {

    public static void migrate(){
        String url = DatabaseConfig.getConfigValue("CRM_DB_URL", "jdbc:postgresql://localhost:5432/crm_db");
        String user = DatabaseConfig.getConfigValue("CRM_DB_User", "postgres");
        String password = DatabaseConfig.getConfigValue("CRM_DB_PASSWORD", null);

        if(password == null || password.isBlank()){
            throw new DatabaseException("CRM_DB_PASSWORD is not set", null);
        }

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}
