package com.pjsoft.uml;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CodeEntity} class.
 * This test class verifies the functionality of adding fields and methods
 * to a {@link CodeEntity} instance. It ensures that fields and methods
 * are correctly added, their properties are accurately stored, and the
 * size of the respective collections is updated as expected.
 */
class CodeEntityTest {
    private static final Logger logger = LoggerFactory.getLogger(CodeEntityTest.class);

    /**
     * Tests the addition of a field to a {@link CodeEntity} instance.
     * Verifies that the field is correctly added, its properties are stored accurately,
     * and the size of the fields collection is updated as expected.
     */
    @Test
    void testAddField_ShouldAddFieldToCodeEntity() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        FieldEntity field = new FieldEntity("fieldName", "String");
        codeEntity.addField(field);
        logger.info("after addField: testAddField_ShouldAddFieldToCodeEntity in CodeEntityTest");

        assertAll(
            () -> assertEquals(1, codeEntity.getFields().size(), "Fields collection size should be 1"),
            () -> {
                FieldEntity retrievedField = new ArrayList<>(codeEntity.getFields()).get(0);
                assertEquals("fieldName", retrievedField.getName(), "Field name should match");
                assertEquals("String", retrievedField.getType(), "Field type should match");
            }
        );
        logger.info("after assertAll: testAddField_ShouldAddFieldToCodeEntity in CodeEntityTest");
    }

    /**
     * Tests the addition of a method to a {@link CodeEntity} instance.
     * Verifies that the method is correctly added, its properties are stored accurately,
     * and the size of the methods collection is updated as expected.
     */
    @Test
    void testAddMethod_ShouldAddMethodToCodeEntity() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        MethodEntity method = new MethodEntity("methodName", "void");
        codeEntity.addMethod(method);
        logger.info("after addMethod: CodeEntityTest");

        assertAll(
            () -> assertEquals(1, codeEntity.getMethods().size(), "Methods collection size should be 1"),
            () -> {
                MethodEntity retrievedMethod = new ArrayList<>(codeEntity.getMethods()).get(0);
                assertEquals("methodName", retrievedMethod.getName(), "Method name should match");
                assertEquals("void", retrievedMethod.getReturnType(), "Method return type should match");
            }
        );
        logger.info("after assertAll: CodeEntityTest");
    }

    /**
     * Tests adding a field with an empty name to a {@link CodeEntity} instance.
     * Verifies that an exception is thrown when attempting to add a field with an empty name.
     */
    @Test
    void testAddFieldWithEmptyName_ShouldThrowException() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        FieldEntity field = new FieldEntity("", "String");
        assertThrows(IllegalArgumentException.class, () -> codeEntity.addField(field), "Adding field with empty name should throw exception");
        logger.info("after assertThrows: CodeEntityTest");
    }

    /**
     * Tests adding a method with an empty name to a {@link CodeEntity} instance.
     * Verifies that an exception is thrown when attempting to add a method with an empty name.
     */
    @Test
    void testAddMethodWithEmptyName_ShouldThrowException() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        MethodEntity method = new MethodEntity("", "void");
        logger.info("before assertThrows: CodeEntityTest");
        assertThrows(IllegalArgumentException.class, () -> codeEntity.addMethod(method), "Adding method with empty name should throw exception");
        logger.info("after assertThrows: CodeEntityTest");
    }

    /**
     * Tests adding a field with special characters in its name to a {@link CodeEntity} instance.
     * Verifies that the field is added successfully and its properties are stored accurately.
     */
    @Test
    void testAddFieldWithSpecialCharacters_ShouldAddField() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        FieldEntity field = new FieldEntity("field@Name#", "String");

        codeEntity.addField(field);
        logger.info("after addField in CodeEntityTest");

        assertAll(
            () -> assertEquals(1, codeEntity.getFields().size(), "Fields collection size should be 1"),
            () -> {
                FieldEntity retrievedField = new ArrayList<>(codeEntity.getFields()).get(0);
                assertEquals("field@Name#", retrievedField.getName(), "Field name should match");
                assertEquals("String", retrievedField.getType(), "Field type should match");
            }
        );

        logger.info("after assertAll: CodeEntityTest");
    }

    /**
     * Tests adding a method with special characters in its name to a {@link CodeEntity} instance.
     * Verifies that the method is added successfully and its properties are stored accurately.
     */
    @Test
    void testAddMethodWithSpecialCharacters_ShouldAddMethod() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        MethodEntity method = new MethodEntity("method@Name#", "void");
        codeEntity.addMethod(method);
        logger.info("after addMethod in CodeEntityTest");

        assertAll(
            () -> assertEquals(1, codeEntity.getMethods().size(), "Methods collection size should be 1"),
            () -> {
                MethodEntity retrievedMethod = new ArrayList<>(codeEntity.getMethods()).get(0);
                assertEquals("method@Name#", retrievedMethod.getName(), "Method name should match");
                assertEquals("void", retrievedMethod.getReturnType(), "Method return type should match");
            }
        );

        logger.info("after assertAll: CodeEntityTest");
    }
}