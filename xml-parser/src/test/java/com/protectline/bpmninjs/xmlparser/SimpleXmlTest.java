package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SimpleXmlTest {
    
    @Test
    public void testSimpleXmlParsing() {
        String simpleXml = """
            <root>
                <element attr="value">content</element>
                <selfClosing attr="value" />
                <parent>
                    <child>child content</child>
                </parent>
            </root>
            """;
        
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        
        assertDoesNotThrow(() -> {
            Element rootElement = parser.getRootElement(simpleXml, tokenDefinition);
            System.out.println("Parsed structure:");
            System.out.println(rootElement);
            System.out.println("\nRegenerated XML:");
            System.out.println(rootElement.toXml());
        });
    }
}