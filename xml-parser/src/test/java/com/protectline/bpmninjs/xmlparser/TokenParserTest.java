package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenParserTest {

    private TokenParser tokenParser;

    @BeforeEach
    void setUp() {
        tokenParser = new TokenParser();
    }

    @Test
    void should_parse_js_project_function_element() {
        // Given - JS Project format
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.STRING, " id"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "230"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "function d(){}"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseTokensToElements(tokens);

        // Then
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        assertThat(element.getElementName()).isEqualTo("function");
        assertThat(element.getAttributes()).containsEntry("id", "230");
        assertThat(element.getContent()).isEqualTo("function d(){}");
    }

    @Test
    void should_parse_bpmn_task_element() {
        // Given - BPMN format
        List<Token> tokens = Arrays.asList(
            new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.ELEMENT, "bpmn:task"),
                new Token(TOKEN_TYPE.STRING, " id"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.STRING, "\"task1\""),
            new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "Task content"),
            new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.ELEMENT, "bpmn:task"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseTokensToElements(tokens);

        // Then
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        assertThat(element.getElementName()).isEqualTo("bpmn:task");
        assertThat(element.getAttributes()).containsEntry("id", "\"task1\"");
        assertThat(element.getContent()).isEqualTo("Task content");
    }

    @Test
    void should_parse_element_without_attributes() {
        // Given
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "some content"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),

            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseTokensToElements(tokens);

        // Then
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        assertThat(element.getElementName()).isEqualTo("main");
        assertThat(element.getAttributes()).isEmpty();
        assertThat(element.getContent()).isEqualTo("some content");
    }

    @Test
    void should_validate_matching_open_close_marks() {
        // Given - Balises non correspondantes
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When & Then
        assertThatThrownBy(() -> tokenParser.parseTokensToElements(tokens))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Mismatched tags: function != main");
    }
    
    @Test
    void should_detect_missing_close_mark() {
        // Given - Balise fermante manquante
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content")
            // Pas de balise fermante !
        );

        // When & Then
        assertThatThrownBy(() -> tokenParser.parseTokensToElements(tokens))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Missing CLOSE_MARK for element: function");
    }
}
