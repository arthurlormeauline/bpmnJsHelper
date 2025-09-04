package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.parser.Element;
import com.protectline.bpmninjs.xmlparser.util.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleXmlTest {
    
    @Test
    void should_parse_simple_xml_with_mixed_elements() {
        // Given
        String simpleXml = """
            <root>
                <element attr="value">content</element>
                <selfClosing attr="value" />
                <parent>
                    <child>child content</child>
                </parent>
            </root>
            """;
        
        // When
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        Element rootElement = parser.getRootElement(simpleXml, tokenDefinition);
        String regeneratedXml = rootElement.toXml();
        
        // Then - Verify structure
        assertThat(rootElement.getElementName()).isEqualTo("root");
        assertThat(rootElement.getChildren()).hasSize(3);
        
        // Verify element with content
        Element elementWithContent = rootElement.getChildren().stream()
            .filter(child -> "element".equals(child.getElementName()))
            .findFirst()
            .orElseThrow();
        assertThat(elementWithContent.getContent()).isEqualTo("content");
        assertThat(elementWithContent.getAttributes()).containsEntry("attr", "\"value\"");
        
        // Verify self-closing element
        Element selfClosing = rootElement.getChildren().stream()
            .filter(child -> "selfClosing".equals(child.getElementName()))
            .findFirst()
            .orElseThrow();
        assertThat(selfClosing.isSelfClosing()).isTrue();
        assertThat(selfClosing.getAttributes()).containsEntry("attr", "\"value\"");
        
        // Verify nested element
        Element parent = rootElement.getChildren().stream()
            .filter(child -> "parent".equals(child.getElementName()))
            .findFirst()
            .orElseThrow();
        assertThat(parent.getChildren()).hasSize(1);
        assertThat(parent.getChildren().get(0).getElementName()).isEqualTo("child");
        assertThat(parent.getChildren().get(0).getContent()).isEqualTo("child content");
        
        // Verify XML regeneration
        assertThat(regeneratedXml).isNotEmpty();
        assertThat(regeneratedXml).contains("<root>");
        assertThat(regeneratedXml).contains("</root>");
        assertThat(regeneratedXml).contains("<element attr=\"value\">content</element>");
        assertThat(regeneratedXml).contains("<selfClosing attr=\"value\" />");
    }
}
