package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;

public class XmlDeclarationTest {
    
    @Test
    public void testSimpleXmlDeclarationParsing() {
        String simpleXmlDecl = """
            <?xml version="1.0" encoding="UTF-8"?>
            <root>content</root>
            """;
        
        XmlParser parser = new XmlParser();
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        
        System.out.println("Parsing XML with declaration:");
        System.out.println(simpleXmlDecl);
        
        // Let's debug the tokens first
        System.out.println("\nDebugging tokens:");
        var tokens = LexerFactory.tokenizeBpmn(simpleXmlDecl);
        for (int i = 0; i < Math.min(15, tokens.size()); i++) {
            System.out.println(i + ": " + tokens.get(i).getType() + " = '" + tokens.get(i).getValue() + "'");
        }
        
        System.out.println("\nSimplified tokens:");
        TokenSimplifier simplifier = new TokenSimplifier();
        var simplifiedTokens = simplifier.simplifyTokens(tokens);
        for (int i = 0; i < Math.min(10, simplifiedTokens.size()); i++) {
            Token token = simplifiedTokens.get(i);
            if (token.getType() == TOKEN_TYPE.SELF_CLOSE_MARK) {
                SelfCloseMark scm = token.getSelfCloseMark();
                System.out.println(i + ": " + token.getType() + " = " + scm.getElementName() + " " + scm.getAttributes());
            } else {
                System.out.println(i + ": " + token.getType() + " = '" + token.getValue() + "'");
            }
        }
        
        try {
            Element rootElement = parser.getRootElement(simpleXmlDecl, tokenDefinition);
            System.out.println("\nParsed successfully:");
            System.out.println(rootElement);
            System.out.println("\nRegenerated XML:");
            System.out.println(rootElement.toXml());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}