package com.protectline.bpmninjs.xmlparser.parser;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.Token;
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

    // Tests from TokenSimplifierSelfClosingTest - Self-closing functionality
    @Test
    void should_simplify_simple_self_closing_tag() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "element"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(TOKEN_TYPE.SELF_CLOSE_MARK);
        
        SelfCloseMark selfCloseMark = result.get(0).getSelfCloseMark();
        assertThat(selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(selfCloseMark.getAttributes()).isEmpty();
    }

    @Test
    void should_simplify_self_closing_tag_with_attributes() {
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
        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(TOKEN_TYPE.SELF_CLOSE_MARK);
        
        SelfCloseMark selfCloseMark = result.get(0).getSelfCloseMark();
        assertThat(selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(selfCloseMark.getAttributes()).hasSize(1);
        assertThat(selfCloseMark.getAttributes()).containsEntry("attr", "\"value\"");
    }

    @Test
    void should_handle_mixed_open_and_self_closing_tags() {
        // Given
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

        // When
        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(result).hasSize(3);
        
        // First: OPEN_MARK for parent
        assertThat(result.get(0).getType()).isEqualTo(TOKEN_TYPE.OPEN_MARK);
        assertThat(result.get(0).getOpenMark().getElementName()).isEqualTo("parent");
        
        // Second: SELF_CLOSE_MARK for selfClosing
        assertThat(result.get(1).getType()).isEqualTo(TOKEN_TYPE.SELF_CLOSE_MARK);
        assertThat(result.get(1).getSelfCloseMark().getElementName()).isEqualTo("selfClosing");
        
        // Third: CLOSE_MARK for parent
        assertThat(result.get(2).getType()).isEqualTo(TOKEN_TYPE.CLOSE_MARK);
        assertThat(result.get(2).getCloseMark().getElementName()).isEqualTo("parent");
    }

    @Test
    void should_handle_content_between_self_closing_tags() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.STRING, "some content"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.STRING, "selfClosing"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "more content")
        );

        // When
        List<Token> result = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(result).hasSize(3);
        
        assertThat(result.get(0).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(result.get(0).getStringValue()).isEqualTo("some content");
        
        assertThat(result.get(1).getType()).isEqualTo(TOKEN_TYPE.SELF_CLOSE_MARK);
        assertThat(result.get(1).getSelfCloseMark().getElementName()).isEqualTo("selfClosing");
        
        assertThat(result.get(2).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(result.get(2).getStringValue()).isEqualTo("more content");
    }
}
