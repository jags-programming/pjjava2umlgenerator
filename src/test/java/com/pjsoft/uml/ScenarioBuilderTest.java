package com.pjsoft.uml;

import org.junit.jupiter.api.Test;

import com.pjsoft.uml.Relative.RelationshipType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioBuilderTest {
/**
    @Test
    void testGenerateSequenceDiagrams_SingleEntryClass() {
        // Arrange
        CodeEntity classA = new CodeEntity("com.example.ClassA");
        CodeEntity classB = new CodeEntity("com.example.ClassB");

        // ClassA calls ClassB
        classA.addRelative(new Relative(RelationshipType.CALLER_CALLEE, classB, "methodB", "methodA"));

        List<CodeEntity> codeEntities = new ArrayList<>();
        codeEntities.add(classA);
        codeEntities.add(classB);

        ScenarioBuilder builder = new ScenarioBuilder("com.example");

        // Act
        List<Scenario> scenarios = builder.generateSequenceDiagrams(codeEntities);

        // Assert
        assertEquals(1, scenarios.size());
        Scenario scenario = scenarios.get(0);
        assertEquals("com.example.ClassA", scenario.getEntryClass());
        assertEquals(1, scenario.getInteractions().size());

        Interaction interaction = scenario.getInteractions().get(0);
        assertEquals("com.example.ClassA", interaction.getCallerClass());
        assertEquals("methodA", interaction.getCallerMethod());
        assertEquals("com.example.ClassB", interaction.getCalleeClass());
        assertEquals("methodB", interaction.getCalleeMethod());
    }

    @Test
    void testGenerateSequenceDiagrams_MultipleEntryClasses() {
        // Arrange
        CodeEntity classA = new CodeEntity("com.example.ClassA");
        CodeEntity classB = new CodeEntity("com.example.ClassB");
        CodeEntity classC = new CodeEntity("com.example.ClassC");

        // ClassA calls ClassB
        classA.addRelative(new Relative(RelationshipType.CALLER_CALLEE, classB, "methodB", "methodA"));

        // ClassC is independent
        List<CodeEntity> codeEntities = new ArrayList<>();
        codeEntities.add(classA);
        codeEntities.add(classB);
        codeEntities.add(classC);

        ScenarioBuilder builder = new ScenarioBuilder("com.example");

        // Act
        List<Scenario> scenarios = builder.generateSequenceDiagrams(codeEntities);

        // Assert
        assertEquals(2, scenarios.size());
        assertTrue(scenarios.stream().anyMatch(s -> s.getEntryClass().equals("com.example.ClassA")));
        assertTrue(scenarios.stream().anyMatch(s -> s.getEntryClass().equals("com.example.ClassC")));
    }

    @Test
    void testGenerateSequenceDiagrams_FilterNonProjectClasses() {
        // Arrange
        CodeEntity classA = new CodeEntity("com.example.ClassA");
        CodeEntity externalClass = new CodeEntity("org.external.ClassB");

        // ClassA calls an external class
        classA.addRelative(new Relative(RelationshipType.CALLER_CALLEE, externalClass, "methodB", "methodA"));

        List<CodeEntity> codeEntities = new ArrayList<>();
        codeEntities.add(classA);
        codeEntities.add(externalClass);

        ScenarioBuilder builder = new ScenarioBuilder("com.example");

        // Act
        List<Scenario> scenarios = builder.generateSequenceDiagrams(codeEntities);

        // Assert
        assertEquals(1, scenarios.size());
        Scenario scenario = scenarios.get(0);
        assertEquals("com.example.ClassA", scenario.getEntryClass());
        assertEquals(0, scenario.getInteractions().size()); // External class is filtered out
    }

    @Test
    void testGenerateSequenceDiagrams_HandleCyclicRelationships() {
        // Arrange
        CodeEntity classA = new CodeEntity("com.example.ClassA");
        CodeEntity classB = new CodeEntity("com.example.ClassB");

        // ClassA calls ClassB, and ClassB calls ClassA (cyclic relationship)
        classA.addRelative(new Relative(RelationshipType.CALLER_CALLEE, classB, "methodB", "methodA"));
        classB.addRelative(new Relative(RelationshipType.CALLER_CALLEE, classA, "methodA", "methodB"));

        List<CodeEntity> codeEntities = new ArrayList<>();
        codeEntities.add(classA);
        codeEntities.add(classB);

        ScenarioBuilder builder = new ScenarioBuilder("com.example");

        // Act
        List<Scenario> scenarios = builder.generateSequenceDiagrams(codeEntities);

        // Assert
        assertEquals(1, scenarios.size());
        Scenario scenario = scenarios.get(0);
        assertEquals("com.example.ClassA", scenario.getEntryClass());
        assertEquals(2, scenario.getInteractions().size()); // Both interactions are captured without infinite loop
    }

    */
}