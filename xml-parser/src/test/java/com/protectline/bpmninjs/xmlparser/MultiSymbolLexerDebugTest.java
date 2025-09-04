package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;

public class MultiSymbolLexerDebugTest {
    
    @Test
    public void testSimpleXmlAfterDeclaration() {
        String simpleCase = "<?xml?><root></root>";
        
        System.out.println("Testing: " + simpleCase);
        
        var tokens = LexerFactory.tokenizeBpmn(simpleCase);
        
        System.out.println("Tokens:");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(i + ": " + tokens.get(i).getType() + " = '" + tokens.get(i).getValue() + "'");
        }
    }
    
    @Test
    public void testEvenSimplerCase() {
        String simpleCase = "?><root>";
        
        System.out.println("Testing: " + simpleCase);
        
        var tokens = LexerFactory.tokenizeBpmn(simpleCase);
        
        System.out.println("Tokens:");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(i + ": " + tokens.get(i).getType() + " = '" + tokens.get(i).getValue() + "'");
        }
    }
}