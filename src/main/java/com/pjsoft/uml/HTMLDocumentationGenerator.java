package com.pjsoft.uml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates an HTML documentation file with a list of diagrams.
 */
public class HTMLDocumentationGenerator {

    /**
     * Generates an HTML documentation file.
     *
     * @param outputDir      the directory containing the diagram image files.
     * @param outputFilePath the path to the generated HTML file.
     * @throws IOException if an error occurs while writing the HTML file.
     */
    public void generateHTMLDocumentation(String outputDir, String htmlDocDirectory) throws IOException {
        // Validate the output directory
        File dir = new File(outputDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException("The output directory does not exist or is not a directory: " + outputDir);
        }

        // Use the provided htmlDocDirectory or create a default one
        File htmlDir = (htmlDocDirectory == null || htmlDocDirectory.isEmpty())
                ? new File("htmldoc")
                : new File(htmlDocDirectory);

        if (!htmlDir.exists()) {
            if (!htmlDir.mkdirs()) {
                throw new IOException("Failed to create HTML documentation directory: " + htmlDir.getAbsolutePath());
            }
        }

        // Create the images directory inside htmlDocDirectory
        File imagesDir = new File(htmlDir, "images");
        if (!imagesDir.exists()) {
            if (!imagesDir.mkdirs()) {
                throw new IOException("Failed to create images directory: " + imagesDir.getAbsolutePath());
            }
        }

        // Find all image files in the output directory
        List<File> imageFiles = findImageFiles(dir);

        // Copy image files to the images directory
        for (File imageFile : imageFiles) {
            File targetFile = new File(imagesDir, imageFile.getName());
            java.nio.file.Files.copy(imageFile.toPath(), targetFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        // Generate the HTML content
        String htmlContent = generateHTMLContent(imageFiles);

        // Write the HTML content to index.html in the htmlDocDirectory
        File htmlFile = new File(htmlDir, "index.html");
        try (FileWriter writer = new FileWriter(htmlFile)) {
            writer.write(htmlContent);
        }

        System.out.println("HTML documentation generated successfully at: " + htmlFile.getAbsolutePath());
    }

    /**
     * Recursively finds image files in the specified directory.
     *
     * @param dir the directory to search.
     * @return a list of image files.
     */
    private List<File> findImageFiles(File dir) {
        List<File> imageFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    imageFiles.addAll(findImageFiles(file));
                } else if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles;
    }

    /**
     * Generates the HTML content for the documentation.
     *
     * @param imageFiles the list of image files to include in the documentation.
     * @return the generated HTML content as a string.
     */
    private String generateHTMLContent(List<File> imageFiles) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"en\">");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.append("<title>UML Diagrams Documentation</title>");
        html.append("<style>");
        html.append("body { display: flex; font-family: Arial, sans-serif; }");
        html.append("#fileList { width: 20%; background-color: #f4f4f4; padding: 10px; overflow-y: auto; }");
        html.append("#contentPane { width: 80%; padding: 10px; }");
        html.append("#fileList a { display: block; margin: 5px 0; text-decoration: none; color: #333; }");
        html.append("#fileList a:hover { text-decoration: underline; }");
        html.append("img { max-width: 100%; height: auto; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div id=\"fileList\">");

        // Add links to the image files
        for (File file : imageFiles) {
            String fileName = file.getName();
            html.append("<a href=\"#").append(fileName).append("\">").append(fileName).append("</a>");
        }

        html.append("</div>");
        html.append("<div id=\"contentPane\">");

        // Add images with anchors
        for (File file : imageFiles) {
            String fileName = file.getName();
            html.append("<h2 id=\"").append(fileName).append("\">").append(fileName).append("</h2>");
            html.append("<img src=\"images/").append(fileName).append("\" alt=\"").append(fileName).append("\">");
        }

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
