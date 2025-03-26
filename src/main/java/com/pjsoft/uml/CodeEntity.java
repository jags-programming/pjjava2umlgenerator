package com.pjsoft.uml;

import java.util.*;

/**
 * Represents a Java Class or Interface.
 */
public class CodeEntity {
    private String name;
    private List<MethodEntity> methods;
    private List<FieldEntity> fields;
    private List<RelationshipManager> relationships;

    public String getName() {
        return name;
    }

    public List<MethodEntity> getMethods() {
        return methods;
    }

    public List<FieldEntity> getFields() {
        return fields;
    }

    public List<RelationshipManager> getRelationships() {
        return relationships;
    }

    public CodeEntity(String name) {
        this.name = name;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    public void addMethod(MethodEntity method) {
        methods.add(method);
    }

    public void addField(FieldEntity field) {
        fields.add(field);
    }

    public void addRelationship(RelationshipManager relationship) {
        relationships.add(relationship);
    }
}