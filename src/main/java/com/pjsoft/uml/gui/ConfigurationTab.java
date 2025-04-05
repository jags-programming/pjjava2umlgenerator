package com.pjsoft.uml.gui;

import com.pjsoft.uml.ConfigurationManager;
import com.pjsoft.uml.UMLDiagramGenerator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ConfigurationTab {

    private final VBox layout;

    public ConfigurationTab(Stage primaryStage) {
        // Welcome Banner
        Label bannerLabel = new Label("Welcome to the UML Diagram Generator!");
        bannerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: blue;");

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

        VBox configOptionsBox = new VBox(10, configOptionsLabel, defaultSettingsOption, fileSettingsOption, customSettingsOption);

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
        layout = new VBox(15, bannerLabel, configOptionsBox, customInputsBox, generateButton, progressIndicator, messageLabel);
        layout.setPadding(new Insets(20)); // Add padding around the entire layout
    }

    public VBox getLayout() {
        return layout;
    }
}
