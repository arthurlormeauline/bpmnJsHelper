package com.protectline.bpmninjs.xmlparser.parser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ElementXmlGenerationTest {

    @Test
    void should_generate_xml_for_self_closing_element() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("id", "test");
        attributes.put("name", "testName");
        
        // When
        Element selfClosingElement = new Element("element", attributes, true);
        String xml = selfClosingElement.toXml();
        
        // Then
        assertThat(xml).contains("<element");
        assertThat(xml).contains("id=test");
        assertThat(xml).contains("name=testName");
        assertThat(xml).contains(" />");
        assertThat(xml).doesNotContain("</element>");
    }

    @Test
    void should_generate_xml_for_element_with_content() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "value");
        
        // When
        Element element = new Element("test", attributes, "content");
        String xml = element.toXml();
        
        // Then
        assertThat(xml.trim()).isEqualTo("<test attr=value>content</test>");
    }

    @Test
    void should_generate_xml_for_element_with_children() {
        // Given
        Element child1 = new Element("child1", new HashMap<>(), "content1");
        Element child2 = new Element("child2", new HashMap<>(), true);
        
        // When
        Element parent = new Element("parent", new HashMap<>(), List.of(child1, child2));
        String xml = parent.toXml();
        
        // Then
        assertThat(xml).contains("<parent>");
        assertThat(xml).contains("<child1>content1</child1>");
        assertThat(xml).contains("<child2 />");
        assertThat(xml).contains("</parent>");
        assertThat(xml).doesNotContain("content1content2"); // Ensure proper formatting
    }

    @Test
    void should_generate_xml_for_empty_element() {
        // Given
        Element element = new Element("empty", new HashMap<>(), "");
        
        // When
        String xml = element.toXml();
        
        // Then
        assertThat(xml.trim()).isEqualTo("<empty></empty>");
    }

    @Test
    void should_handle_root_element_without_name() {
        // Given - Root element with empty name (artificial root)
        Element child = new Element("child", new HashMap<>(), "content");
        Element root = new Element("", new HashMap<>(), List.of(child));
        
        // When
        String xml = root.toXml();
        
        // Then - The artificial root should not appear in XML
        assertThat(xml).doesNotContain("<>");
        assertThat(xml).doesNotContain("</>");
        assertThat(xml).contains("<child>content</child>");
    }

    @Test
    void should_generate_properly_indented_nested_xml() {
        // Given
        Element grandchild = new Element("grandchild", new HashMap<>(), "deep content");
        Element child = new Element("child", new HashMap<>(), List.of(grandchild));
        Element parent = new Element("parent", new HashMap<>(), List.of(child));
        
        // When
        String xml = parent.toXml();
        
        // Then
        assertThat(xml).contains("<parent>");
        assertThat(xml).contains("  <child>");
        assertThat(xml).contains("    <grandchild>deep content</grandchild>");
        assertThat(xml).contains("  </child>");
        assertThat(xml).contains("</parent>");
        // Verify proper indentation structure
        assertThat(xml).matches("(?s).*<parent>\\s*<child>\\s*<grandchild>.*</grandchild>\\s*</child>\\s*</parent>.*");
    }
}