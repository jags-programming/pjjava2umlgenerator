package com.pjsoft.uml;

import java.util.*;

/**
 * ScenarioBuilder
 * 
 * Responsible for generating scenarios (sequence diagrams) from CodeEntity objects.
 * A scenario represents a sequence of interactions starting from a specific entry class
 * and method, traversing the caller-callee relationships recursively.
 */
public class ScenarioBuilder {

    private final String projectPackage;

    /**
     * Constructs a ScenarioBuilder with the specified project package.
     * 
     * @param projectPackage The package name to filter project-specific classes.
     */
    public ScenarioBuilder(String projectPackage) {
        this.projectPackage = projectPackage;
    }

    /**
     * Identifies entry classes (classes that are not callees).
     * 
     * An entry class is a class that is not called by any other class in the provided
     * list of CodeEntity objects. These classes serve as starting points for scenarios.
     * 
     * @param codeEntities The list of CodeEntity objects representing the parsed classes.
     * @return A list of entry classes.
     * @throws IllegalArgumentException If the codeEntities list is null.
     */
    private List<CodeEntity> identifyEntryClasses(List<CodeEntity> codeEntities) {
        if (codeEntities == null) {
            throw new IllegalArgumentException("CodeEntities cannot be null");
        }

        Map<String, Boolean> calleeMap = new HashMap<>();

        // Mark all classes that are callees
        for (CodeEntity codeEntity : codeEntities) {
            for (Relative relative : codeEntity.getRelatives()) {
                if (relative.getRelationshipType() == Relative.RelationshipType.CALLER_CALLEE) {
                    calleeMap.put(relative.getCalleeEntity().getName(), true);
                }
            }
        }

        // Collect classes that are not callees
        List<CodeEntity> entryClasses = new ArrayList<>();
        for (CodeEntity codeEntity : codeEntities) {
            if (!calleeMap.containsKey(codeEntity.getName())) {
                entryClasses.add(codeEntity);
            }
        }

        return entryClasses;
    }

    /**
     * Generates a list of scenarios from the provided CodeEntity objects.
     * 
     * This method identifies entry classes and generates scenarios for each entry class
     * and its methods. Each scenario represents a sequence diagram starting from a specific
     * method of an entry class.
     * 
     * @param codeEntities The list of CodeEntity objects representing the parsed classes.
     * @return A list of scenarios representing sequence diagrams.
     * @throws IllegalArgumentException If the codeEntities list is null.
     */
    public List<Scenario> getScenarios(List<CodeEntity> codeEntities) {
        if (codeEntities == null) {
            throw new IllegalArgumentException("CodeEntities cannot be null");
        }

        // Step 1: Identify entry classes
        List<CodeEntity> entryClasses = identifyEntryClasses(codeEntities);

        // Step 2: Build scenarios for each entry class and its methods
        List<Scenario> scenarios = new ArrayList<>();
        for (CodeEntity entryClass : entryClasses) {
            // Get scenarios for the current entry class
            List<Scenario> entryClassScenarios = getScenariosByEntryClass(entryClass);

            // Merge the scenarios into the main list
            scenarios.addAll(entryClassScenarios);
        }

        return scenarios; // Return the list of all scenarios for all entry classes
    }

    /**
     * Generates a list of scenarios for a given entry class.
     * 
     * This method iterates over all methods of the entry class and generates a separate
     * scenario for each method. Each scenario represents a sequence diagram starting
     * from the given method and traversing the caller-callee relationships recursively.
     * 
     * @param entryClass The entry class to generate scenarios for.
     * @return A list of scenarios starting from the entry class.
     * @throws IllegalArgumentException If the entryClass is null.
     */
    private List<Scenario> getScenariosByEntryClass(CodeEntity entryClass) {
        if (entryClass == null) {
            throw new IllegalArgumentException("EntryClass cannot be null");
        }

        List<Scenario> scenarios = new ArrayList<>();

        // Iterate over all methods of the entry class
        for (MethodEntity method : entryClass.getMethods()) {
            // Create a new scenario for each method
            Scenario scenario = new Scenario(entryClass.getName(), method.getName());
            Set<String> visited = new HashSet<>(); // To avoid cycles

            // Build the scenario starting from this method
            buildScenarioFromMethod(entryClass, method.getName(), scenario, visited);

            // Add the built scenario to the list
            scenarios.add(scenario);
        }

        return scenarios;
    }

    /**
 * Recursively builds a scenario starting from a specific method of an entry class.
 * 
 * This method traverses the caller-callee relationships recursively, adding interactions
 * to the same scenario. It uses a visited set to avoid cycles in the relationship graph.
 * 
 * @param entryClass The entry class to start the scenario.
 * @param startingMethod The method in the entry class that starts the scenario.
 * @param scenario The scenario to build.
 * @param visited A set of visited classes and methods to avoid cycles.
 * @throws IllegalArgumentException If any of the arguments are null.
 */
private void buildScenarioFromMethod(CodeEntity entryClass, String startingMethod, Scenario scenario, Set<String> visited) {
    if (entryClass == null || startingMethod == null || scenario == null) {
        throw new IllegalArgumentException("Arguments cannot be null");
    }

    // Create a unique key for the current class and method to track visited nodes
    String visitedKey = entryClass.getName() + "::" + startingMethod;
    if (visited.contains(visitedKey)) {
        return; // Avoid cycles
    }
    visited.add(visitedKey);

    // Get all CALLER_CALLEE relationships for the entry class
    List<Relative> callerCalleeRelatives = entryClass.getRelativesByRelationshipType(Relative.RelationshipType.CALLER_CALLEE);

    for (Relative relative : callerCalleeRelatives) {
        // Check if the relationship starts from the given method
        if (relative.getCallerMethod().equals(startingMethod)) {
            CodeEntity calleeEntity = relative.getCalleeEntity();

            // Add the interaction to the scenario
            scenario.addInteraction(new Interaction(
                entryClass.getName(),
                relative.getCallerMethod(),
                calleeEntity.getName(),
                relative.getCalleeMethod()
            ));

            // Recursively build the scenario for the callee
            buildScenarioFromMethod(calleeEntity, relative.getCalleeMethod(), scenario, visited);
        }
    }
}

    /**
     * Recursively builds a scenario starting from a specific method of an entry class.
     * 
     * This method traverses the caller-callee relationships recursively, adding interactions
     * to the scenario. It uses a visited set to avoid cycles in the relationship graph.
     * 
     * @param entryClass The entry class to start the scenario.
     * @param startingMethod The method in the entry class that starts the scenario.
     * @param scenario The scenario to build.
     * @param visited A set of visited classes and methods to avoid cycles.
     * @throws IllegalArgumentException If any of the arguments are null.
     */
    /**
    private void buildScenarioFromMethod(CodeEntity entryClass, String startingMethod, Scenario scenario, Set<String> visited) {
        if (entryClass == null || startingMethod == null || scenario == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        String visitedKey = entryClass.getName() + "::" + startingMethod;
        if (visited.contains(visitedKey)) {
            return; // Avoid cycles
        }
        visited.add(visitedKey);

        // Get all CALLER_CALLEE relationships for the entry class
        List<Relative> callerCalleeRelatives = entryClass.getRelativesByRelationshipType(Relative.RelationshipType.CALLER_CALLEE);

        for (Relative relative : callerCalleeRelatives) {
            // Check if the relationship starts from the given method
            if (relative.getCallerMethod().equals(startingMethod)) {
                CodeEntity calleeEntity = relative.getCalleeEntity();

                // Add the interaction to the scenario
                scenario.addInteraction(new Interaction(
                    entryClass.getName(),
                    relative.getCallerMethod(),
                    calleeEntity.getName(),
                    relative.getCalleeMethod()
                ));

                // Recursively build the scenario for the callee
                buildScenarioFromMethod(calleeEntity, relative.getCalleeMethod(), scenario, visited);
                //when you make current callee as caller and its method as starting method and iterate further, interactions should be added to same scenario otherwise it will be a new scenario. It won't make full directed graph or call tree required for sequence diagram. This is the problem in current implmentation that is why a sequence diagram is showing first class, its next class but after than no class their is callee of second class in the chain. 
            }
        }
    }
        */
}