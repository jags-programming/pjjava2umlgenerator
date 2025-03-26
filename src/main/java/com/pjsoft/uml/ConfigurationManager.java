package com.pjsoft.uml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration Manager (Singleton)
 * 
 * Manages application configurations.
 */
public class ConfigurationManager {
     private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static ConfigurationManager instance;
    private Map<String, String> settings;

    private ConfigurationManager() {
        settings = new HashMap<>();
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void loadConfig(String filePath) throws IOException {
        // Load settings logic
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            // Log the exception
            logger.error("Error reading configuration file: " + e.getMessage());
            // Throw a custom exception with a more informative message
            throw new IOException("Failed to load configuration from file: " + filePath, e);
        }
    }

    public String getConfig(String key) {
        return settings.getOrDefault(key, "");
    }

        /**
     * Validates the loaded configuration to ensure all required keys are present.
     * 
     * @throws IllegalArgumentException if any required configuration is missing or invalid.
     */
    public void validateConfig() {
        List<String> requiredKeys = Arrays.asList(
            "input.directory",
            "output.directory",
            "diagram.types",
            "plantuml.path",
            "logging.level"
        );

        for (String key : requiredKeys) {
            if (!settings.containsKey(key) || settings.get(key).isEmpty()) {
                throw new IllegalArgumentException("Missing or empty required configuration: " + key);
            }
        }

        // Additional validation for specific keys
        String diagramTypes = settings.get("diagram.types");
        if (!diagramTypes.matches("(class|sequence|activity|state|usecase)(,(class|sequence|activity|state|usecase))*")) {
            throw new IllegalArgumentException("Invalid value for 'diagram.types': " + diagramTypes);
        }

        logger.info("Configuration validated successfully.");
    }
}