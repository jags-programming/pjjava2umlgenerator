package com.pjsoft.uml.gui;


import com.pjsoft.uml.ConfigurationManager;
import com.pjsoft.uml.UMLDiagramGenerator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ConfigurationTab {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConfigurationTab.class);

    private final VBox layout;
    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public ConfigurationTab(Stage primaryStage) {
        // Welcome message
        Label bannerLabel = new Label("Welcome to UML Generator");
        bannerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Radio buttons for themes
        ToggleGroup themeToggleGroup = new ToggleGroup();

        RadioButton lightTheme = new RadioButton("Light");
        lightTheme.setToggleGroup(themeToggleGroup);
        lightTheme.setOnAction(e -> setStyle(scene, "style.light"));

        RadioButton darkTheme = new RadioButton("Dark");
        darkTheme.setToggleGroup(themeToggleGroup);
        darkTheme.setOnAction(e -> setStyle(scene, "style.dark"));

        RadioButton pastelTheme = new RadioButton("Pastel");
        pastelTheme.setToggleGroup(themeToggleGroup);
        pastelTheme.setOnAction(e -> setStyle(scene, "style.pastel"));

        // Default selection
        lightTheme.setSelected(true);

        // HBox for radio buttons
        HBox themeBox = new HBox(10, lightTheme, darkTheme, pastelTheme);

        // Align everything nicely
        themeBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(themeBox, Priority.NEVER);
        HBox.setHgrow(bannerLabel, Priority.ALWAYS);

        // Top row combining welcome + themes
        HBox topRow = new HBox();
        topRow.setPadding(new Insets(10));
        topRow.setSpacing(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        // topRow.getChildren().addAll(bannerLabel, themeBox);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topRow.getChildren().addAll(bannerLabel, spacer, themeBox);

        // Configuration Options
        Label configOptionsLabel = new Label("How do you want to proceed?");
        configOptionsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        RadioButton defaultSettingsOption = new RadioButton("Use default settings");
        RadioButton fileSettingsOption = new RadioButton("Load settings from a configuration file");
        RadioButton customSettingsOption = new RadioButton("Enter custom inputs interactively");
        ToggleGroup configOptionsGroup = new ToggleGroup();
        defaultSettingsOption.setToggleGroup(configOptionsGroup);
        fileSettingsOption.setToggleGroup(configOptionsGroup);
        customSettingsOption.setToggleGroup(configOptionsGroup);
        defaultSettingsOption.setSelected(true);

        VBox configOptionsBox = new VBox(10, configOptionsLabel, defaultSettingsOption, fileSettingsOption,
                customSettingsOption);

        // Input Fields for Custom Configuration
        Label inputDirLabel = new Label("Input Directory:");
        TextField inputDirField = new TextField();
        Button inputDirButton = new Button("Browse...");
        inputDirButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Input Directory");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                inputDirField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        Label outputDirLabel = new Label("Output Directory:");
        TextField outputDirField = new TextField();
        Button outputDirButton = new Button("Browse...");
        outputDirButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Output Directory");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                outputDirField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        Label diagramTypesLabel = new Label("Diagram Types (class,sequence):");
        TextField diagramTypesField = new TextField("class,sequence");

        Label includePackageLabel = new Label("Include Package (default: all):");
        TextField includePackageField = new TextField();

        GridPane customInputsGrid = new GridPane();
        customInputsGrid.setHgap(10);
        customInputsGrid.setVgap(10);
        customInputsGrid.setPadding(new Insets(10));
        customInputsGrid.add(inputDirLabel, 0, 0);
        customInputsGrid.add(inputDirField, 1, 0);
        customInputsGrid.add(inputDirButton, 2, 0);
        customInputsGrid.add(outputDirLabel, 0, 1);
        customInputsGrid.add(outputDirField, 1, 1);
        customInputsGrid.add(outputDirButton, 2, 1);
        customInputsGrid.add(diagramTypesLabel, 0, 2);
        customInputsGrid.add(diagramTypesField, 1, 2);
        customInputsGrid.add(includePackageLabel, 0, 3);
        customInputsGrid.add(includePackageField, 1, 3);

        TitledPane customInputsPane = new TitledPane("Custom Inputs", customInputsGrid);
        customInputsPane.setCollapsible(false);

        VBox customInputsBox = new VBox(10, customInputsPane);
        customInputsBox.setVisible(false);

        // Show/Hide Custom Inputs Based on Selection
        customSettingsOption.setOnAction(event -> customInputsBox.setVisible(true));
        defaultSettingsOption.setOnAction(event -> customInputsBox.setVisible(false));
        fileSettingsOption.setOnAction(event -> customInputsBox.setVisible(false));

        // Progress Indicator
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);

        // Success and Error Messages
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

        // Generate Button
        Button generateButton = new Button("Generate UML Diagrams");
        generateButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        generateButton.setOnAction(event -> {
            progressIndicator.setVisible(true);
            messageLabel.setText("Generating UML diagrams...");
            messageLabel.setStyle("-fx-text-fill: blue;");

            // Run the diagram generation in a background thread
            new Thread(() -> {
                try {
                    ConfigurationManager configManager = ConfigurationManager.getInstance();
                    configManager.clearSettings();

                    if (defaultSettingsOption.isSelected()) {
                        configManager.loadDefaultConfig();
                    } else if (fileSettingsOption.isSelected()) {
                        configManager.loadFileConfig("config/application.properties");
                    } else if (customSettingsOption.isSelected()) {
                        configManager.setProperty("input.directory", inputDirField.getText());
                        configManager.setProperty("output.directory", outputDirField.getText());
                        configManager.setProperty("diagram.types", diagramTypesField.getText());
                        configManager.setProperty("include.package", includePackageField.getText());
                    }

                    UMLDiagramGenerator generator = new UMLDiagramGenerator(configManager);
                    generator.generateDiagrams();

                    // Update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                        messageLabel.setText("UML diagrams generated successfully!");
                        messageLabel.setStyle("-fx-text-fill: green;");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                        messageLabel.setText("Error: " + e.getMessage());
                        messageLabel.setStyle("-fx-text-fill: red;");
                    });
                }
            }).start();
        });

        // Add padding to the parent VBox
 
        layout = new VBox(15, topRow, configOptionsBox, customInputsBox, generateButton, progressIndicator,
                messageLabel);

        layout.setPadding(new Insets(20)); // Add padding around the entire layout
    }

    public VBox getLayout() {
        return layout;
    }

    private void setStyle(Scene scene, String styleProperty) {
        if (scene == null) {
            logger.error("Scene not set");
            return;
        }
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();

        String cssFilePath = configurationManager.getProperty(styleProperty);

        File cssFile = new File(cssFilePath);
        if (cssFile.exists()) {
            scene.getStylesheets().clear();

            scene.getStylesheets().add(cssFile.toURI().toString());
        } else {
            logger.error("Stylesheet not found at: " + cssFile.getAbsolutePath());
            logger.error("Either application.properties is not loaded or property in file is missing");
            logger.info("Going to load styles from default path if it exists");

            String style;
            switch (styleProperty) {
                case "style.dark":
                    style = "style_dark.css";
                    break;
                case "style.light":
                    style = "style_light.css";
                    break;
                case "style.pastel":
                    style = "style_pastel.css";
                    break;
                default:
                    style = "style_light.css";
                    break;
            }

            File cssFromDefaultLocation = new File("styles/" + style);

            if (cssFromDefaultLocation.exists()) {
                scene.getStylesheets().clear();

                scene.getStylesheets().add(cssFromDefaultLocation.toURI().toString());
                logger.info("Style loaded from default location: " + cssFromDefaultLocation.toURI().toString());
            } else {

                logger.error("css files not present in default location, can't apply theme.");
            }
        }

    }
}