package org.resumeoptimizer.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Singleton
public class FileConfig {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private final Properties properties;
    private static FileConfig instance;

    public FileConfig(String configFilePath) throws IOException {
        properties = new Properties();
        try (FileReader reader = new FileReader(configFilePath)) {
            properties.load(reader);
        } catch (IOException e) {
            // handle exception
            System.err.println("Failed to load configuration file: " + configFilePath);
            System.err.println("Working directory: " + System.getProperty("user.dir"));
            throw e;
        }
    }

    public String getSetting(String key) {
        return properties.getProperty(key);
    }

    public static FileConfig getInstance(String configFilePath) throws IOException {
        if (instance == null) {
            instance = new FileConfig(configFilePath);
        }
        return instance;
    }

    public static FileConfig getInstance() throws IOException {
        return getInstance(CONFIG_FILE_PATH);
    }
}