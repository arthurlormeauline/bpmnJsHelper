package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenSimplifierSelfClosingTest {

    private TokenSimplifier tokenSimplifier;

    @BeforeEach
    public void setUp() {
        tokenSimplifier = new TokenSimplifier();
    }

    @Test
    public void testSimpleSelfClosingTag() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        assertEquals(1, result.size());
        assertEquals(TOKEN_TYPE.SELF_CLOSE_MARK, result.get(0).getType());
        
        SelfCloseMark selfCloseMark = result.get(0).getSelfCloseMark();
        assertEquals("element", selfCloseMark.getElementName());
        assertTrue(selfCloseMark.getAttributes().isEmpty());
    }

    @Test
    public void testSelfClosingTagWithAttributes() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element attr"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"value\""),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        assertEquals(1, result.size());
        assertEquals(TOKEN_TYPE.SELF_CLOSE_MARK, result.get(0).getType());
        
        SelfCloseMark selfCloseMark = result.get(0).getSelfCloseMark();
        assertEquals("element", selfCloseMark.getElementName());
        assertEquals(1, selfCloseMark.getAttributes().size());
        assertEquals("\"value\"", selfCloseMark.getAttributes().get("attr"));
    }

    @Test
    public void testMixedOpenAndSelfClosingTags() {
        List<Token> tokens = List.of(
            // <parent>
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "parent"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            // <selfClosing />
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "selfClosing"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            // </parent>
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.STRING, "parent"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        assertEquals(3, result.size());
        
        // First: OPEN_MARK for parent
        assertEquals(TOKEN_TYPE.OPEN_MARK, result.get(0).getType());
        assertEquals("parent", result.get(0).getOpenMark().getElementName());
        
        // Second: SELF_CLOSE_MARK for selfClosing
        assertEquals(TOKEN_TYPE.SELF_CLOSE_MARK, result.get(1).getType());
        assertEquals("selfClosing", result.get(1).getSelfCloseMark().getElementName());
        
        // Third: CLOSE_MARK for parent
        assertEquals(TOKEN_TYPE.CLOSE_MARK, result.get(2).getType());
        assertEquals("parent", result.get(2).getCloseMark().getElementName());
    }

    @Test
    public void testContentBetweenTags() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.STRING, "some content"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "selfClosing"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "more content")
        );

        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        assertEquals(3, result.size());
        
        assertEquals(TOKEN_TYPE.CONTENT, result.get(0).getType());
        assertEquals("some content", result.get(0).getStringValue());
        
        assertEquals(TOKEN_TYPE.SELF_CLOSE_MARK, result.get(1).getType());
        assertEquals("selfClosing", result.get(1).getSelfCloseMark().getElementName());
        
        assertEquals(TOKEN_TYPE.CONTENT, result.get(2).getType());
        assertEquals("more content", result.get(2).getStringValue());
    }
}