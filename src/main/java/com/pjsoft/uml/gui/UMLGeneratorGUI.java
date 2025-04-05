package com.pjsoft.uml.gui;

import java.io.File;

import com.pjsoft.uml.ConfigurationManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UMLGeneratorGUI extends Application {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UMLGeneratorGUI.class);

    @Override
    public void start(Stage primaryStage) {
        // Create TabPane
        TabPane tabPane = new TabPane();

        // Add Configuration Tab
        ConfigurationTab configurationTab = new ConfigurationTab(primaryStage);

        Tab configTab = new Tab("Configuration", configurationTab.getLayout());
        configTab.setClosable(false);

        // Add Preview Tab
        Tab previewTab = new Tab("Diagram Preview", new PreviewTab(primaryStage).getLayout());
        previewTab.setClosable(false);

        tabPane.getTabs().addAll(configTab, previewTab);

        // Set up Scene and Stage
        Scene scene = new Scene(tabPane, 800, 600);
        configurationTab.setScene(scene);
        String styleProperty = "style.light"; // To get from user input setting by toggle or other means

        setStyle(scene, styleProperty);
        primaryStage.setTitle("PJ Java2UML Diagram Generator");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/favicon-32x32.png")));
        primaryStage.show();
    }

    private void setStyle(Scene scene, String styleProperty) {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();

        String cssFilePath = configurationManager.getProperty(styleProperty);

        File cssFile = new File(cssFilePath);
        if (cssFile.exists()) {

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
                scene.getStylesheets().add(cssFromDefaultLocation.toURI().toString());
                logger.info("Style loaded from default location: " + cssFromDefaultLocation.toURI().toString());
            } else {

                logger.error("css files not present in default location, can't apply theme.");
            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}