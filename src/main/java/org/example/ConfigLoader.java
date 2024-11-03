package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ConfigLoader {

    private static Properties properties = new Properties();

    public ConfigLoader(String filePath) {
        loadProperties(filePath);
    }

    // Method to get a property by name
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    private void loadProperties(String filePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("File not found in classpath: " + filePath);
                return;
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
