package com.pjsoft.uml;

/**
 * Represents a Field within a Java Class.
 */
public class FieldEntity {
    private String name;
    private String type;
    private String visibility;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public FieldEntity(String name, String type) {
        this.name = name;
        this.type = type;
    }
}