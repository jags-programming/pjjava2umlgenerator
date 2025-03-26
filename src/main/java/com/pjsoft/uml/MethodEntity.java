package com.pjsoft.uml;

import java.util.*;

/**
 * Represents a Method within a Java Class.
 */
public class MethodEntity {
    private String name;
    private String returnType;
    private List<String> parameters;
    private String visibility;

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getVisibility() {
        return visibility;
    }

    public MethodEntity(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
    }

    public void addParameter(String paramType) {
        parameters.add(paramType);
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}