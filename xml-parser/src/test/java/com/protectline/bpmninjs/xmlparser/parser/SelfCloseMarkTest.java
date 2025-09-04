package com.protectline.bpmninjs.xmlparser.parser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SelfCloseMarkTest {

    @Test
    public void testSelfCloseMarkCreation() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("id", "test");
        attributes.put("name", "testName");
        
        SelfCloseMark selfCloseMark = new SelfCloseMark("element", attributes);
        
        assertEquals("element", selfCloseMark.getElementName());
        assertEquals(attributes, selfCloseMark.getAttributes());
        assertEquals("test", selfCloseMark.getAttributes().get("id"));
        assertEquals("testName", selfCloseMark.getAttributes().get("name"));
    }

    @Test
    public void testSelfCloseMarkToString() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "value");
        
        SelfCloseMark selfCloseMark = new SelfCloseMark("test", attributes);
        String stringRepr = selfCloseMark.toString();
        
        assertTrue(stringRepr.contains("test"));
        assertTrue(stringRepr.contains("attr"));
        assertTrue(stringRepr.contains("value"));
    }

    @Test
    public void testSelfCloseMarkWithEmptyAttributes() {
        SelfCloseMark selfCloseMark = new SelfCloseMark("element", new HashMap<>());
        
        assertEquals("element", selfCloseMark.getElementName());
        assertTrue(selfCloseMark.getAttributes().isEmpty());
    }
}