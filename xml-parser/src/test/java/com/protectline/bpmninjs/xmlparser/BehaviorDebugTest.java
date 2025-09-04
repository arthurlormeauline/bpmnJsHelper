package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class BehaviorDebugTest {
    
    @Test
    public void debugNewVsOldBehavior() {
        TokenParser tokenParser = new TokenParser();
        
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("element", Map.of("attr", "value")))
        );
        
        System.out.println("=== ANCIEN COMPORTEMENT (artificial root) ===");
        try {
            java.lang.reflect.Method method = TokenParser.class.getDeclaredMethod("parseTokensToArtificialRoot", List.class);
            method.setAccessible(true);
            Element artificialRoot = (Element) method.invoke(tokenParser, tokens);
            System.out.println("Artificial root name: '" + artificialRoot.getElementName() + "'");
            System.out.println("Artificial root children: " + artificialRoot.getChildren().size());
            for (Element child : artificialRoot.getChildren()) {
                System.out.println("  Child: " + child.getElementName());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n=== NOUVEAU COMPORTEMENT (parseXml) ===");
        Element documentRoot = tokenParser.parseXml(tokens);
        System.out.println("Document root name: '" + documentRoot.getElementName() + "'");
        System.out.println("Document root children: " + documentRoot.getChildren().size());
        
        System.out.println("\n=== COMPORTEMENT PARSEXML_AND_GET_ELEMENTS ===");
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);
        System.out.println("Elements size: " + elements.size());
        for (int i = 0; i < elements.size(); i++) {
            System.out.println("  Element " + i + ": " + elements.get(i).getElementName());
        }
    }
}