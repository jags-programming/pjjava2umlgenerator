package com.pjsoft.uml.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UMLGeneratorGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create TabPane
        TabPane tabPane = new TabPane();

        // Add Configuration Tab
        Tab configTab = new Tab("Configuration", new ConfigurationTab(primaryStage).getLayout());
        configTab.setClosable(false);

        // Add Preview Tab
        Tab previewTab = new Tab("Diagram Preview", new PreviewTab(primaryStage).getLayout());
        previewTab.setClosable(false);

        tabPane.getTabs().addAll(configTab, previewTab);

        // Set up Scene and Stage
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("PJ Java2UML Diagram Generator");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/favicon-32x32.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}