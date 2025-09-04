package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenParserTreeTest {

    private TokenParser tokenParser;

    @BeforeEach
    public void setUp() {
        tokenParser = new TokenParser();
    }

    @Test
    public void testParseSingleSelfClosingElement() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("element", java.util.Map.of("attr", "value")))
        );

        Element root = tokenParser.parseXml(tokens);
        
        assertNotNull(root);
        assertEquals("element", root.getElementName()); // Document root réel, pas fictif
        assertEquals(0, root.getChildren().size()); // Pas d'enfants pour un self-closing element
        assertTrue(root.isSelfClosing());
        assertEquals("value", root.getAttributes().get("attr"));
    }

    @Test
    public void testParseElementWithContent() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("element", java.util.Map.of("attr", "value"))),
            new Token(TOKEN_TYPE.CONTENT, "content"),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("element"))
        );

        Element root = tokenParser.parseXml(tokens);
        
        assertNotNull(root);
        assertEquals("element", root.getElementName()); // Document root réel
        assertEquals(0, root.getChildren().size()); // Pas d'enfants, juste du contenu
        assertFalse(root.isSelfClosing());
        assertEquals("content", root.getContent());
        assertEquals("value", root.getAttributes().get("attr"));
    }

    @Test
    public void testParseNestedElements() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("parent", java.util.Map.of())),
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("child", java.util.Map.of())),
            new Token(TOKEN_TYPE.CONTENT, "child content"),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("child")),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("parent"))
        );

        Element root = tokenParser.parseXml(tokens);
        
        assertNotNull(root);
        assertEquals("parent", root.getElementName()); // Document root est "parent"
        assertEquals(1, root.getChildren().size());
        
        Element child = root.getChildren().get(0);
        assertEquals("child", child.getElementName());
        assertEquals("child content", child.getContent());
    }

    @Test
    public void testParseMixedContent() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("parent", java.util.Map.of())),
            new Token(TOKEN_TYPE.CONTENT, "text before"),
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("selfClosing", java.util.Map.of())),
            new Token(TOKEN_TYPE.CONTENT, "text after"),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("parent"))
        );

        Element root = tokenParser.parseXml(tokens);
        
        assertNotNull(root);
        assertEquals("parent", root.getElementName()); // Document root est "parent"
        assertEquals(1, root.getChildren().size()); // selfClosing child
        
        Element selfClosingChild = root.getChildren().get(0);
        assertEquals("selfClosing", selfClosingChild.getElementName());
        assertTrue(selfClosingChild.isSelfClosing());
    }

    @Test
    public void testParseMultipleTopLevelElements() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("first", java.util.Map.of())),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("first")),
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("second", java.util.Map.of())),
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("third", java.util.Map.of())),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("third"))
        );

        Element root = tokenParser.parseXml(tokens);
        
        assertNotNull(root);
        // parseXml retourne le premier élément comme document root
        assertEquals("first", root.getElementName()); 
        assertEquals(0, root.getChildren().size()); // "first" est vide
        
        // Pour voir tous les éléments, il faut utiliser parseXmlAndGetElements()
        List<Element> allElements = tokenParser.parseXmlAndGetElements(tokens);
        assertEquals(3, allElements.size()); // 3 éléments (SANS artificial root)
        assertEquals("first", allElements.get(0).getElementName());
        assertEquals("second", allElements.get(1).getElementName());
        assertEquals("third", allElements.get(2).getElementName());
    }

    @Test 
    public void testBackwardsCompatibilityGetElements() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("parent", java.util.Map.of())),
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("child", java.util.Map.of())),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("parent"))
        );

        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);
        
        // Devrait retourner tous les éléments dans une liste plate (SANS le root fictif)
        assertEquals(2, elements.size()); // parent, child (pas le root fictif)
        
        // Éléments réels uniquement 
        assertEquals("parent", elements.get(0).getElementName());
        assertEquals("child", elements.get(1).getElementName());
    }
}