package com.pjsoft.uml;

/**
 * Manages relationships between classes.
 */
public class RelationshipManager {
    private CodeEntity source;
    private CodeEntity target;
    private String type;

    public RelationshipManager(CodeEntity source, CodeEntity target, String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }
}