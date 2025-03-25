package com.pjsoft.uml;

/**
 * Represents a Field within a Java Class.
 */
public class FieldEntity {
    private String name;
    private String type;
    private String visibility;

    public FieldEntity(String name, String type) {
        this.name = name;
        this.type = type;
    }
}