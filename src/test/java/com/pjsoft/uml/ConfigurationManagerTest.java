package com.pjsoft.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationManagerTest {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ConfigurationManagerTest.class);
    private ConfigurationManager configManager;
    private Path tempConfigFile;

    @BeforeEach
    void setUp() throws IOException {
        configManager = ConfigurationManager.getInstance();
        tempConfigFile = Files.createTempFile("application", ".properties");
        configManager.clearSettings(); // Clear the settings map
    }

    /**
    @Test
    void testLoadConfig() throws IOException {
        logger.info("Enters: testLoadConfig()");
        // Write sample configuration to the temp file
        try (FileWriter writer = new FileWriter(tempConfigFile.toFile())) {
            logger.info("before write in testLoadConfig");
            writer.write("key1=value1\n");
            writer.write("key2=value2\n");
            logger.info("after write in testLoadConfig");
        }

        // Load the configuration
        logger.info("before configManager.loadConfig(tempConfigFile.toString())");
        configManager.loadConfig(tempConfigFile.toString());
        logger.info("after configManager.loadConfig(tempConfigFile.toString())");

        // Verify the configuration values
        assertEquals("value1", configManager.getProperty("key1")); // Updated
        assertEquals("value2", configManager.getProperty("key2")); // Updated
        logger.info("after assertEquals");
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

    @Test
    void testValidateConfigMissingRequiredKeys() throws IOException {
        // Write an incomplete configuration to the temp file
        logger.debug("Enters: testValidateConfigMissingRequiredKeys");
        try (FileWriter writer = new FileWriter(tempConfigFile.toFile())) {
            writer.write("input.directory=/path/to/input\n");
        }
        logger.debug("testValidateConfigMissingRequiredKeys: written an incomplete configuration to the temp file.");
        // Load the configuration
        configManager.loadConfig(tempConfigFile.toString());
        logger.debug("testValidateConfigMissingRequiredKeys: called: configManager.loadConfig(tempConfigFile.toString()) ");
        // Verify that validation fails due to missing required keys
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            configManager.validateConfig();
        });
        logger.debug("testValidateConfigMissingRequiredKeys()>exception.getMessage(): "+ exception.getMessage());
        assertTrue(exception.getMessage().contains("Missing or empty required configuration"));
    }

    @Test
    void testValidateDirectories() throws IOException {
        // Dynamically construct non-existent paths
        String inputDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "nonexistentInput";
        String outputDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "nonexistentOutput";

        // Write a configuration with all required keys
        try (FileWriter writer = new FileWriter(tempConfigFile.toFile())) {
            writer.write("input.directory=" + inputDir + "\n");
            writer.write("output.directory=" + outputDir + "\n");
            writer.write("diagram.types=class\n");
          
            writer.write("logging.level=INFO\n");
        }

        // Load the configuration
        configManager.loadConfig(tempConfigFile.toString());

        // Verify that validation fails due to non-existent directories
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            configManager.validateConfig();
        });

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void testValidateDiagramTypes() throws IOException {
        // Write a configuration with invalid diagram types and all required keys
        try (FileWriter writer = new FileWriter(tempConfigFile.toFile())) {
            writer.write("input.directory=/path/to/input\n");
            writer.write("output.directory=/path/to/output\n");
            writer.write("diagram.types=invalidType\n");
            
            writer.write("logging.level=INFO\n");
        }

        // Load the configuration
        configManager.loadConfig(tempConfigFile.toString());

        // Verify that validation fails due to invalid diagram types
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            configManager.validateConfig();
        });

        assertTrue(exception.getMessage().contains("Invalid value for 'diagram.types'"));
    }

    @Test
    void testLoadDefaultSettings() {
        logger.debug("LoadDefaultSettings test going to call loadDefaultSettings of config maanger");
        // Load default settings
        configManager.loadDefaultSettings();

        // Verify default values
        assertEquals("./input", configManager.getProperty("input.directory"));
        assertEquals("./output", configManager.getProperty("output.directory"));
        assertEquals("class,sequence", configManager.getProperty("diagram.types"));
        
    }

  */  

}