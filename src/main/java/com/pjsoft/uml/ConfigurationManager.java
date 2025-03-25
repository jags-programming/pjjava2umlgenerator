package com.pjsoft.uml;

import java.util.*;

/**
 * Configuration Manager (Singleton)
 * 
 * Manages application configurations.
 */
public class ConfigurationManager {
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

    public void loadConfig(String filePath) {
        // Load settings logic
    }

    public String getConfig(String key) {
        return settings.getOrDefault(key, "");
    }
}