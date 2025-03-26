package com.pjsoft.uml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationManagerTest {
    private ConfigurationManager configManager;
    private Path tempConfigFile;

    @BeforeEach
    void setUp() throws IOException {
        configManager = ConfigurationManager.getInstance();
        tempConfigFile = Files.createTempFile("config", ".properties");
    }

    @Test
    void testLoadConfig() throws IOException {
        // Write sample configuration to the temp file
        try (FileWriter writer = new FileWriter(tempConfigFile.toFile())) {
            writer.write("key1=value1\n");
            writer.write("key2=value2\n");
        }

        // Load the configuration
        configManager.loadConfig(tempConfigFile.toString());

        // Verify the configuration values
        assertEquals("value1", configManager.getConfig("key1"));
        assertEquals("value2", configManager.getConfig("key2"));
    }

    @Test
    void testLoadConfigFileNotFound() {
        // Attempt to load a non-existent configuration file
        IOException exception = assertThrows(IOException.class, () -> {
            configManager.loadConfig("non_existent_file.properties");
        });

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Failed to load configuration from file"));
    }
}