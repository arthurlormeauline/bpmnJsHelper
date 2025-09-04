package com.protectline.bpmninjs.xmlparser.lexer.tokendefinition;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimplifiedTokenDefinitionSelfClosingTest {

    @Test
    public void testIsSelfCloseMark() {
        // Pattern: [OPEN, STRING, END_SYMBOL, CLOSE] = <element />
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        assertTrue(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 0));
        assertFalse(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 1));
        assertFalse(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 2));
    }

    @Test
    public void testIsSelfCloseMarkWithAttributes() {
        // Pattern: [OPEN, STRING, EQUALS, STRING, END_SYMBOL, CLOSE] = <element attr="value" />
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element attr"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"value\""),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        assertTrue(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 0));
    }

    @Test
    public void testParseSelfCloseMarkSimple() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        SimplifiedTokenDefinition.SelfCloseMarkResult result = 
            SimplifiedTokenDefinition.parseSelfCloseMark(tokens, 0);
        
        assertEquals("element", result.selfCloseMark.getElementName());
        assertTrue(result.selfCloseMark.getAttributes().isEmpty());
        assertEquals(4, result.nextIndex);
    }

    @Test
    public void testParseSelfCloseMarkWithSingleAttribute() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element attr"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"value\""),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        SimplifiedTokenDefinition.SelfCloseMarkResult result = 
            SimplifiedTokenDefinition.parseSelfCloseMark(tokens, 0);
        
        assertEquals("element", result.selfCloseMark.getElementName());
        assertEquals(1, result.selfCloseMark.getAttributes().size());
        assertEquals("\"value\"", result.selfCloseMark.getAttributes().get("attr"));
        assertEquals(6, result.nextIndex);
    }

    @Test
    public void testParseSelfCloseMarkWithMultipleAttributes() {
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element id name"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"test\""),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"testName\""),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        SimplifiedTokenDefinition.SelfCloseMarkResult result = 
            SimplifiedTokenDefinition.parseSelfCloseMark(tokens, 0);
        
        assertEquals("element", result.selfCloseMark.getElementName());
        assertEquals(2, result.selfCloseMark.getAttributes().size());
        assertEquals("\"test\"", result.selfCloseMark.getAttributes().get("id"));
        assertEquals("\"testName\"", result.selfCloseMark.getAttributes().get("name"));
        assertEquals(8, result.nextIndex);
    }
}
