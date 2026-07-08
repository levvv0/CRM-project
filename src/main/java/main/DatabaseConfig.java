package main;

public class DatabaseConfig {

    public static String getConfigValue(String name, String defaultValue){
        String propertyValue = System.getProperty(name);

        if(propertyValue != null && !propertyValue.isBlank()){
            return propertyValue;
        }

        String envValue = System.getenv(name);

        if(envValue != null && !envValue.isBlank()){
            return envValue;
        }

        return defaultValue;
    }
}
