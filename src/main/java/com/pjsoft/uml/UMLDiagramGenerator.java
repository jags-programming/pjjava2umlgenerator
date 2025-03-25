package com.pjsoft.uml;

import java.util.*;

/**
 * UML Diagram Generator
 * 
 * Author: PJSoft Team
 * Version: 1.0
 * Description: This program generates UML diagrams from Java source code.
 */
public class UMLDiagramGenerator {
    private List<CodeEntity> parsedData;
    private String diagramFormat;
    private ClassDiagramService classDiagramService;
    private SequenceDiagramService sequenceDiagramService;
    private StorageService storageService;
    private ConfigurationManager config;

    public UMLDiagramGenerator(ConfigurationManager config) {
        this.config = config;
        this.classDiagramService = new ClassDiagramService(config);
        this.sequenceDiagramService = new SequenceDiagramService(config);
        this.storageService = new StorageService(config);
    }

    public void loadParsedData(List<CodeEntity> data) {
        this.parsedData = data;
    }

    public String generateClassDiagram() {
        return classDiagramService.generateDiagram(parsedData);
    }

    public String generateSequenceDiagram() {
        return sequenceDiagramService.generateDiagram(parsedData);
    }

    public void exportDiagram(String format, String outputPath) {
        storageService.storeDiagram(format, outputPath);
    }

    public void storeDiagram() {
        // Logic to store generated diagrams
    }
}