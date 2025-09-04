package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ElementXmlGenerationTest {

    @Test
    public void testSelfClosingElementXmlGeneration() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("id", "test");
        attributes.put("name", "testName");
        
        Element selfClosingElement = new Element("element", attributes, true);
        String xml = selfClosingElement.toXml();
        
        assertTrue(xml.contains("<element"));
        assertTrue(xml.contains("id=test"));
        assertTrue(xml.contains("name=testName"));
        assertTrue(xml.contains(" />"));
        assertFalse(xml.contains("</element>"));
    }

    @Test
    public void testElementWithContentXmlGeneration() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "value");
        
        Element element = new Element("test", attributes, "content");
        String xml = element.toXml();
        
        assertTrue(xml.contains("<test attr=value>content</test>"));
    }

    @Test
    public void testElementWithChildrenXmlGeneration() {
        Element child1 = new Element("child1", new HashMap<>(), "content1");
        Element child2 = new Element("child2", new HashMap<>(), true);
        
        Element parent = new Element("parent", new HashMap<>(), List.of(child1, child2));
        String xml = parent.toXml();
        
        assertTrue(xml.contains("<parent>"));
        assertTrue(xml.contains("<child1>content1</child1>"));
        assertTrue(xml.contains("<child2 />"));
        assertTrue(xml.contains("</parent>"));
    }

    @Test
    public void testEmptyElementXmlGeneration() {
        Element element = new Element("empty", new HashMap<>(), "");
        String xml = element.toXml();
        
        assertTrue(xml.contains("<empty></empty>"));
    }

    @Test
    public void testRootElementXmlGeneration() {
        Element child = new Element("child", new HashMap<>(), "content");
        Element root = new Element("", new HashMap<>(), List.of(child));
        
        String xml = root.toXml();
        
        // L'élément root fictif ne devrait pas apparaître dans le XML
        assertFalse(xml.contains("<>"));
        assertFalse(xml.contains("</>"));
        assertTrue(xml.contains("<child>content</child>"));
    }

    @Test
    public void testNestedElementsXmlGeneration() {
        Element grandchild = new Element("grandchild", new HashMap<>(), "deep content");
        Element child = new Element("child", new HashMap<>(), List.of(grandchild));
        Element parent = new Element("parent", new HashMap<>(), List.of(child));
        
        String xml = parent.toXml();
        
        assertTrue(xml.contains("<parent>"));
        assertTrue(xml.contains("  <child>"));
        assertTrue(xml.contains("    <grandchild>deep content</grandchild>"));
        assertTrue(xml.contains("  </child>"));
        assertTrue(xml.contains("</parent>"));
    }
}