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
        Element selfClosingElement = Element.Builder.builder()
                .withElementName("element")
                .withAttributes(attributes)
                .withSelfClosing(true)
                .build();
        String xml = selfClosingElement.toXml();
        
        // Then
        assertThat(xml).contains("<element");
        assertThat(xml).contains("id=\"test\"");
        assertThat(xml).contains("name=\"testName\"");
        assertThat(xml).contains(" />");
        assertThat(xml).doesNotContain("</element>");
    }

    @Test
    void should_generate_xml_for_element_with_content() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "value");
        
        // When
        Element element = Element.Builder.builder()
                .withElementName("test")
                .withAttributes(attributes)
                .withContent("content")
                .build();
        String xml = element.toXml();
        
        // Then
        assertThat(xml.trim()).isEqualTo("<test attr=\"value\">content</test>");
    }

    @Test
    void should_generate_xml_for_element_with_children() {
        // Given
        Element child1 = Element.Builder.builder()
                .withElementName("child1")
                .withAttributes(new HashMap<>())
                .withContent("content1")
                .build();
        Element child2 = Element.Builder.builder()
                .withElementName("child2")
                .withAttributes(new HashMap<>())
                .withSelfClosing(true)
                .build();
        
        // When
        Element parent = Element.Builder.builder()
                .withElementName("parent")
                .withAttributes(new HashMap<>())
                .withChildren(List.of(child1, child2))
                .build();
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
        Element element = Element.Builder.builder()
                .withElementName("empty")
                .withAttributes(new HashMap<>())
                .withContent("")
                .build();
        
        // When
        String xml = element.toXml();
        
        // Then
        assertThat(xml.trim()).isEqualTo("<empty></empty>");
    }

    @Test
    void should_handle_root_element_without_name() {
        // Given - Root element with empty name (artificial root)
        Element child = Element.Builder.builder()
                .withElementName("child")
                .withAttributes(new HashMap<>())
                .withContent("content")
                .build();
        Element root = Element.Builder.builder()
                .withElementName("")
                .withAttributes(new HashMap<>())
                .withChildren(List.of(child))
                .build();
        
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
        Element grandchild = Element.Builder.builder()
                .withElementName("grandchild")
                .withAttributes(new HashMap<>())
                .withContent("deep content")
                .build();
        Element child = Element.Builder.builder()
                .withElementName("child")
                .withAttributes(new HashMap<>())
                .withChildren(List.of(grandchild))
                .build();
        Element parent = Element.Builder.builder()
                .withElementName("parent")
                .withAttributes(new HashMap<>())
                .withChildren(List.of(child))
                .build();
        
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