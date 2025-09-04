package com.protectline.bpmninjs.xmlparser.lexer.tokendefinition;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimplifiedTokenDefinitionSelfClosingTest {

    @Test
    void should_detect_self_closing_mark_pattern() {
        // Given - Pattern: [OPEN, STRING, END_SYMBOL, CLOSE] = <element />
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        // When & Then
        assertThat(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 0)).isTrue();
        assertThat(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 1)).isFalse();
        assertThat(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 2)).isFalse();
    }

    @Test
    void should_detect_self_closing_mark_pattern_with_attributes() {
        // Given - Pattern: [OPEN, STRING, EQUALS, STRING, END_SYMBOL, CLOSE] = <element attr="value" />
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element attr"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"value\""),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        // When & Then
        assertThat(SimplifiedTokenDefinition.isSelfCloseMark(tokens, 0)).isTrue();
    }

    @Test
    void should_parse_simple_self_closing_mark() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        // When
        SimplifiedTokenDefinition.SelfCloseMarkResult result = 
            SimplifiedTokenDefinition.parseSelfCloseMark(tokens, 0);
        
        // Then
        assertThat(result.selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(result.selfCloseMark.getAttributes()).isEmpty();
        assertThat(result.nextIndex).isEqualTo(4);
    }

    @Test
    void should_parse_self_closing_mark_with_single_attribute() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element attr"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"value\""),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
        
        // When
        SimplifiedTokenDefinition.SelfCloseMarkResult result = 
            SimplifiedTokenDefinition.parseSelfCloseMark(tokens, 0);
        
        // Then
        assertThat(result.selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(result.selfCloseMark.getAttributes()).hasSize(1);
        assertThat(result.selfCloseMark.getAttributes()).containsEntry("attr", "\"value\"");
        assertThat(result.nextIndex).isEqualTo(6);
    }

    @Test
    void should_parse_self_closing_mark_with_multiple_attributes() {
        // Given
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
        
        // When
        SimplifiedTokenDefinition.SelfCloseMarkResult result = 
            SimplifiedTokenDefinition.parseSelfCloseMark(tokens, 0);
        
        // Then
        assertThat(result.selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(result.selfCloseMark.getAttributes()).hasSize(2);
        assertThat(result.selfCloseMark.getAttributes()).containsEntry("id", "\"test\"");
        assertThat(result.selfCloseMark.getAttributes()).containsEntry("name", "\"testName\"");
        assertThat(result.nextIndex).isEqualTo(8);
    }
}
