package com.protectline.bpmninjs.xmlparser.lexer.tokendefinition;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.Token;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimplifiedTokenDefinitionTest {

    @Test
    void should_parse_single_url_attribute_in_open_mark() {
        // Given - OPEN_MARK tokens for: <bpmn:definitions xmlns:bpmn="https://test">
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.STRING, "bpmn:definitions xmlns:bpmn"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "https:"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "test"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        SimplifiedTokenDefinition.OpenMarkResult result = SimplifiedTokenDefinition.parseOpenMark(tokens, 0);

        // Then - Exhaustive verification
        assertThat(result).isNotNull();
        assertThat(result.nextIndex).isEqualTo(10); // Should have consumed all tokens
        assertThat(result.openMark.getElementName()).isEqualTo("bpmn:definitions");
        assertThat(result.openMark.getAttributes()).hasSize(1);
        assertThat(result.openMark.getAttributes()).containsEntry("xmlns:bpmn", "\"https://test\"");
    }

    @Test
    void should_parse_multiple_consecutive_url_attributes_in_open_mark() {
        // Given - OPEN_MARK tokens for: <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI">
        // This is the exact problematic sequence from our LexerTest debug output
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.STRING, "bpmn:definitions xmlns:bpmn"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "http:"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "www.omg.org"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "spec"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "BPMN"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "20100524"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "MODEL"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, " xmlns:bpmndi"),  // Space + next attribute name
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "http:"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "www.omg.org"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "spec"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "BPMN"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "20100524"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "DI"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        SimplifiedTokenDefinition.OpenMarkResult result = SimplifiedTokenDefinition.parseOpenMark(tokens, 0);

        // Then - Both attributes should be parsed correctly
        assertThat(result).isNotNull();
        assertThat(result.nextIndex).isEqualTo(34); // Should have consumed all 34 tokens
        assertThat(result.openMark.getElementName()).isEqualTo("bpmn:definitions");
        
        // Critical verification: BOTH attributes should be present
        assertThat(result.openMark.getAttributes()).hasSize(2);
        assertThat(result.openMark.getAttributes()).containsEntry("xmlns:bpmn", "\"http://www.omg.org/spec/BPMN/20100524/MODEL\"");
        assertThat(result.openMark.getAttributes()).containsEntry("xmlns:bpmndi", "\"http://www.omg.org/spec/BPMN/20100524/DI\"");
        
        // Verify exact URL reconstruction
        String bpmnUrl = result.openMark.getAttributes().get("xmlns:bpmn");
        assertThat(bpmnUrl).isEqualTo("\"http://www.omg.org/spec/BPMN/20100524/MODEL\"");
        
        String bpmndiUrl = result.openMark.getAttributes().get("xmlns:bpmndi");
        assertThat(bpmndiUrl).isEqualTo("\"http://www.omg.org/spec/BPMN/20100524/DI\"");
    }

    private static void debugTokenList(String testName, List<Token> tokens) {
        System.out.println("=== DEBUG " + testName + " ===");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.printf("%2d: %s%n", i, tokens.get(i));
        }
        System.out.println("=== END DEBUG ===");
    }
}