package com.pjsoft.uml.gui;

import com.pjsoft.uml.ConfigurationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewTab {

    private final VBox layout;

    public PreviewTab(Stage primaryStage) {
        // File/Folder Selection
        Label selectionLabel = new Label("Select a folder:");
        TextField pathField = new TextField();
        pathField.setEditable(false);
        Button browseButton = new Button("Browse...");

        // Image Display Area
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);

        // Navigation Buttons
        Button leftButton = new Button("<");
        Button rightButton = new Button(">");
        leftButton.setDisable(true);
        rightButton.setDisable(true);

        HBox navigationBox = new HBox(10, leftButton, rightButton);
        navigationBox.setAlignment(Pos.CENTER);

        // Layout for File/Folder Selection
        HBox selectionBox = new HBox(10, selectionLabel, pathField, browseButton);
        selectionBox.setAlignment(Pos.CENTER_LEFT);

        // VBox Layout for the Tab
        layout = new VBox(15, selectionBox, imageView, navigationBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // List to Store Image Files
        List<File> imageFiles = new ArrayList<>();
        final int[] currentIndex = {0};

        // Load Default Output Directory
        loadDefaultOutputDirectory(imageFiles, pathField, imageView, leftButton, rightButton, currentIndex);

        // Browse Button Action
        browseButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder");

            // Allow user to choose a folder
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                pathField.setText(selectedDirectory.getAbsolutePath());
                imageFiles.clear();

                // Load all image files from the selected directory
                File[] files = selectedDirectory.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
                if (files != null) {
                    imageFiles.addAll(Arrays.asList(files));
                }

                // Display the first image
                if (!imageFiles.isEmpty()) {
                    currentIndex[0] = 0;
                    displayImage(imageFiles.get(currentIndex[0]), imageView);
                    updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
                } else {
                    imageView.setImage(null);
                    leftButton.setDisable(true);
                    rightButton.setDisable(true);
                }
            }
        });

        // Left Button Action
        leftButton.setOnAction(event -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                displayImage(imageFiles.get(currentIndex[0]), imageView);
                updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
            }
        });

        // Right Button Action
        rightButton.setOnAction(event -> {
            if (currentIndex[0] < imageFiles.size() - 1) {
                currentIndex[0]++;
                displayImage(imageFiles.get(currentIndex[0]), imageView);
                updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
            }
        });
    }

    private void loadDefaultOutputDirectory(List<File> imageFiles, TextField pathField, ImageView imageView, Button leftButton, Button rightButton, int[] currentIndex) {
        try {
            ConfigurationManager configManager = ConfigurationManager.getInstance();

            // Retrieve the output directory
            String outputDirectory = configManager.getProperty("output.directory");
            if (outputDirectory == null || outputDirectory.isEmpty()) {
                outputDirectory = configManager.getDefaultSettings().get("output.directory");
            }

            if (outputDirectory != null) {
                File outputDir = new File(outputDirectory);
                if (outputDir.exists() && outputDir.isDirectory()) {
                    pathField.setText(outputDirectory);

                    // Load all image files from the output directory
                    File[] files = outputDir.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
                    if (files != null) {
                        imageFiles.addAll(Arrays.asList(files));
                    }

                    // Display the first image
                    if (!imageFiles.isEmpty()) {
                        currentIndex[0] = 0;
                        displayImage(imageFiles.get(currentIndex[0]), imageView);
                        updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
                    } else {
                        // No images found, leave the TextField blank
                        pathField.clear();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading default output directory: " + e.getMessage());
            pathField.clear();
        }
    }

    private void displayImage(File file, ImageView imageView) {
        try {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(null);
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    private void updateNavigationButtons(Button leftButton, Button rightButton, int currentIndex, int totalImages) {
        leftButton.setDisable(currentIndex == 0);
        rightButton.setDisable(currentIndex == totalImages - 1);
    }

    public VBox getLayout() {
        return layout;
    }
}
