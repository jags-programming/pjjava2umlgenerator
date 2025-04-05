package com.pjsoft.uml.gui;

import com.pjsoft.uml.ConfigurationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewTab {

    private final BorderPane layout;

    public PreviewTab(Stage primaryStage) {
        // File/Folder Selection
        Label selectionLabel = new Label("Select a folder:");
        TextField pathField = new TextField();
        pathField.setEditable(false);
        Button browseButton = new Button("Browse...");

        // Error Message Label
        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorMessageLabel.setVisible(false); // Hidden by default

        // Image Display Area
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);

        // Add Zoom Functionality
        Scale scale = new Scale(1, 1, 0, 0);
        imageView.getTransforms().add(scale);

        // ScrollPane for ImageView
        ScrollPane imageContainer = new ScrollPane(imageView);
        imageContainer.setPannable(true);
        imageContainer.setFitToWidth(true);
        imageContainer.setFitToHeight(true);
        imageContainer.setStyle("-fx-background-color: #f0f0f0;");
        imageContainer.setPadding(new Insets(10));

        // Toolbar with Zoom In and Zoom Out Buttons
        Button zoomInButton = new Button("Zoom In");
        Button zoomOutButton = new Button("Zoom Out");

        // Zoom In Button Action
        zoomInButton.setOnAction(event -> {
            double newScaleX = scale.getX() * 1.1;
            double newScaleY = scale.getY() * 1.1;

            // Limit zoom levels
            if (newScaleX <= 5) {
                scale.setX(newScaleX);
                scale.setY(newScaleY);
            }
        });

        // Zoom Out Button Action
        zoomOutButton.setOnAction(event -> {
            double newScaleX = scale.getX() * 0.9;
            double newScaleY = scale.getY() * 0.9;

            // Limit zoom levels
            if (newScaleX >= 0.5) {
                scale.setX(newScaleX);
                scale.setY(newScaleY);
            }
        });

        ToolBar toolBar = new ToolBar(zoomInButton, zoomOutButton);

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
        selectionBox.setPadding(new Insets(10));

        // Combine Selection Box and Error Message
        VBox topSection = new VBox(10, selectionBox, errorMessageLabel);

        // BorderPane Layout
        layout = new BorderPane();
        layout.setTop(new VBox(toolBar, topSection)); // Add toolbar and top section
        layout.setCenter(imageContainer);
        layout.setBottom(navigationBox);

        // List to Store Image Files
        List<File> imageFiles = new ArrayList<>();
        final int[] currentIndex = {0};

        // Load Default Output Directory
        loadDefaultOutputDirectory(imageFiles, pathField, imageView, leftButton, rightButton, currentIndex, scale, errorMessageLabel);

        // Browse Button Action
        browseButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder");

            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                pathField.setText(selectedDirectory.getAbsolutePath());
                imageFiles.clear();

                File[] files = selectedDirectory.listFiles((dir, name) ->
                        name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
                if (files != null) {
                    imageFiles.addAll(Arrays.asList(files));
                }

                if (!imageFiles.isEmpty()) {
                    currentIndex[0] = 0;
                    resetImageView(imageView, scale);
                    displayImage(imageFiles.get(currentIndex[0]), imageView);
                    updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
                    errorMessageLabel.setVisible(false); // Hide error message
                } else {
                    imageView.setImage(null);
                    leftButton.setDisable(true);
                    rightButton.setDisable(true);
                    errorMessageLabel.setText("No images found in the selected directory.");
                    errorMessageLabel.setVisible(true); // Show error message
                }
            }
        });

        leftButton.setOnAction(event -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                resetImageView(imageView, scale);
                displayImage(imageFiles.get(currentIndex[0]), imageView);
                updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
            }
        });

        rightButton.setOnAction(event -> {
            if (currentIndex[0] < imageFiles.size() - 1) {
                currentIndex[0]++;
                resetImageView(imageView, scale);
                displayImage(imageFiles.get(currentIndex[0]), imageView);
                updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
            }
        });
    }

    private void loadDefaultOutputDirectory(List<File> imageFiles, TextField pathField, ImageView imageView,
                                            Button leftButton, Button rightButton, int[] currentIndex, Scale scale,
                                            Label errorMessageLabel) {
        try {
            ConfigurationManager configManager = ConfigurationManager.getInstance();
            String outputDirectory = configManager.getProperty("output.directory");
            if (outputDirectory == null || outputDirectory.isEmpty()) {
                outputDirectory = configManager.getDefaultSettings().get("output.directory");
            }

            if (outputDirectory != null) {
                File outputDir = new File(outputDirectory);
                if (outputDir.exists() && outputDir.isDirectory()) {
                    pathField.setText(outputDirectory);
                    File[] files = outputDir.listFiles((dir, name) ->
                            name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
                    if (files != null) {
                        imageFiles.addAll(Arrays.asList(files));
                    }

                    if (!imageFiles.isEmpty()) {
                        currentIndex[0] = 0;
                        resetImageView(imageView, scale);
                        displayImage(imageFiles.get(currentIndex[0]), imageView);
                        updateNavigationButtons(leftButton, rightButton, currentIndex[0], imageFiles.size());
                        errorMessageLabel.setVisible(false); // Hide error message
                    } else {
                        pathField.clear();
                        errorMessageLabel.setText("No images found in the default output directory.");
                        errorMessageLabel.setVisible(true); // Show error message
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading default output directory: " + e.getMessage());
            pathField.clear();
            errorMessageLabel.setText("Error loading default output directory.");
            errorMessageLabel.setVisible(true); // Show error message
        }
    }

    private void resetImageView(ImageView imageView, Scale scale) {
        scale.setX(1);
        scale.setY(1);
        imageView.setTranslateX(0);
        imageView.setTranslateY(0);
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

    public BorderPane getLayout() {
        return layout;
    }
}
