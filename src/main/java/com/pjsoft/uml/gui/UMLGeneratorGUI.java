package com.pjsoft.uml.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.pjsoft.uml.ConfigurationManager;
import com.pjsoft.uml.UMLDiagramGenerator;

import java.util.Map;

public class UMLGeneratorGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
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

        // Input Fields for Custom Configuration
        Label inputDirLabel = new Label("Input Directory:");
        TextField inputDirField = new TextField();
        Label outputDirLabel = new Label("Output Directory:");
        TextField outputDirField = new TextField();
        Label diagramTypesLabel = new Label("Diagram Types (class,sequence):");
        TextField diagramTypesField = new TextField("class,sequence");

        VBox customInputsBox = new VBox(10, inputDirLabel, inputDirField, outputDirLabel, outputDirField, diagramTypesLabel, diagramTypesField);
        customInputsBox.setPadding(new Insets(10));
        customInputsBox.setVisible(false);

        // Show/Hide Custom Inputs Based on Selection
        customSettingsOption.setOnAction(event -> customInputsBox.setVisible(true));
        defaultSettingsOption.setOnAction(event -> customInputsBox.setVisible(false));
        fileSettingsOption.setOnAction(event -> customInputsBox.setVisible(false));

        // Generate Button
        Button generateButton = new Button("Generate UML Diagrams");
        generateButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Success and Error Messages
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

        generateButton.setOnAction(event -> {
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
                messageLabel.setText("UML diagrams generated successfully!");
                messageLabel.setStyle("-fx-text-fill: green;");
            } catch (Exception e) {
                messageLabel.setText("Error: " + e.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // Layout
        VBox layout = new VBox(15, bannerLabel, configOptionsLabel, defaultSettingsOption, fileSettingsOption, customSettingsOption, customInputsBox, generateButton, messageLabel);
        layout.setPadding(new Insets(20));

        // Scene and Stage
        Scene scene = new Scene(layout, 600, 450);
        primaryStage.setTitle("UML Diagram Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}