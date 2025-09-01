package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TokenSimplifierTest {

    private TokenSimplifier tokenSimplifier;

    @BeforeEach
    void setUp() {
        tokenSimplifier = new TokenSimplifier();
    }

    @Test
    void should_simplify_js_project_element_without_attributes() {
        // Given - JS Project format with //<
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.STRING, "main"),
                new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "content"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "main"),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Token> simplified = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(simplified).hasSize(3);

        // OPEN_MARK
        assertThat(simplified.get(0).getType()).isEqualTo(TOKEN_TYPE.OPEN_MARK);
        OpenMark openMark = simplified.get(0).getOpenMark();
        assertThat(openMark.getElementName()).isEqualTo("main");
        assertThat(openMark.getAttributes()).isEmpty();

        // CONTENT
        assertThat(simplified.get(1).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(1).getStringValue()).isEqualTo("content");

        // CLOSE_MARK
        assertThat(simplified.get(2).getType()).isEqualTo(TOKEN_TYPE.CLOSE_MARK);
        CloseMark closeMark = simplified.get(2).getCloseMark();
        assertThat(closeMark.getElementName()).isEqualTo("main");
    }

    @Test
    void should_simplify_bpmn_element_with_attributes() {
        // Given - BPMN format with <
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.STRING, "bpmn:task id"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.STRING, "\"task1\""),
                new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "Task content"),
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "bpmn:task"),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Token> simplified = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(simplified).hasSize(3);

        // OPEN_MARK avec attributs
        assertThat(simplified.get(0).getType()).isEqualTo(TOKEN_TYPE.OPEN_MARK);
        OpenMark openMark = simplified.get(0).getOpenMark();
        assertThat(openMark.getElementName()).isEqualTo("bpmn:task");
        assertThat(openMark.getAttributes()).containsEntry("id", "\"task1\"");

        // CONTENT
        assertThat(simplified.get(1).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(1).getStringValue()).isEqualTo("Task content");

        // CLOSE_MARK
        assertThat(simplified.get(2).getType()).isEqualTo(TOKEN_TYPE.CLOSE_MARK);
        CloseMark closeMark = simplified.get(2).getCloseMark();
        assertThat(closeMark.getElementName()).isEqualTo("bpmn:task");
    }

    @Test
    void should_handle_only_content_without_marks() {
        // Given - Pas de balises, juste du contenu
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.STRING, "just some content"),
                new Token(TOKEN_TYPE.STRING, " and more")
        );

        // When
        List<Token> simplified = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(simplified).hasSize(1);
        assertThat(simplified.get(0).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(0).getStringValue()).isEqualTo("just some content and more");
    }

    @Test
    void should_debug_print_tokens() {
        // Given
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.STRING, "main"),
                new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "content"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "main"),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        System.out.println("=== TOKENS ORIGINAUX (TokenSimplifierTest) ===");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(i + ": " + tokens.get(i));
        }

        // When
        List<Token> simplified = tokenSimplifier.simplifyTokens(tokens);

        System.out.println("\n=== TOKENS SIMPLIFIÉS (TokenSimplifierTest) ===");
        for (int i = 0; i < simplified.size(); i++) {
            System.out.println(i + ": " + simplified.get(i));
        }

        // Then - Ce test sert principalement pour le debug
        assertThat(simplified).isNotEmpty();
    }
}
