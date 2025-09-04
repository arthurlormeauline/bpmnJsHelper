package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.Test;

import java.util.List;

public class TokenParserDebugTest {
    
    @Test
    public void debugTokenParserTreeTest() {
        TokenParser tokenParser = new TokenParser();
        
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("parent", java.util.Map.of())),
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("child", java.util.Map.of())),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("parent"))
        );

        System.out.println("=== INPUT TOKENS ===");
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            System.out.println(i + ": " + token.getType() + " = " + token.getValue());
        }

        System.out.println("\n=== PARSING WITH parseXmlAndGetElements ===");
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);
        System.out.println("Number of elements: " + elements.size());
        
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            System.out.println(i + ": " + element.getElementName() + " (children: " + element.getChildren().size() + ")");
        }

        System.out.println("\n=== PARSING WITH parseXml ===");
        Element rootElement = tokenParser.parseXml(tokens);
        System.out.println("Root element: " + rootElement.getElementName() + " (children: " + rootElement.getChildren().size() + ")");
        
        System.out.println("\n=== PARSING WITH parseTokensToArtificialRoot ===");
        try {
            java.lang.reflect.Method method = TokenParser.class.getDeclaredMethod("parseTokensToArtificialRoot", List.class);
            method.setAccessible(true);
            Element artificialRoot = (Element) method.invoke(tokenParser, tokens);
            System.out.println("Artificial root: " + artificialRoot.getElementName() + " (children: " + artificialRoot.getChildren().size() + ")");
            
            for (int i = 0; i < artificialRoot.getChildren().size(); i++) {
                Element child = artificialRoot.getChildren().get(i);
                System.out.println("  Child " + i + ": " + child.getElementName());
            }
        } catch (Exception e) {
            System.out.println("Error accessing parseTokensToArtificialRoot: " + e.getMessage());
        }
    }
}