package com.pjsoft.uml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeEntityTest {
    @Test
    void testAddField() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        FieldEntity field = new FieldEntity("fieldName", "String"); // Updated to match constructor
        codeEntity.addField(field);

        assertEquals(1, codeEntity.getFields().size());
        assertEquals("fieldName", codeEntity.getFields().get(0).getName());
        assertEquals("String", codeEntity.getFields().get(0).getType());
    }

    @Test
    void testAddMethod() {
        CodeEntity codeEntity = new CodeEntity("TestClass");
        MethodEntity method = new MethodEntity("methodName", "void"); // Updated to match constructor
        codeEntity.addMethod(method);

        assertEquals(1, codeEntity.getMethods().size());
        assertEquals("methodName", codeEntity.getMethods().get(0).getName());
        assertEquals("void", codeEntity.getMethods().get(0).getReturnType());
    }
}