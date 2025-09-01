package com.protectline.bpmninjs.jsproject.parser;

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
    void should_simplify_element_without_attributes() {
        // Given
        List<Token> tokens = Arrays.asList(
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.SLASH, "/"),
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
    void should_simplify_element_with_attributes() {
        // Given
        List<Token> tokens = Arrays.asList(
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.STRING, " id"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "230"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "function d(){}"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.SLASH, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Token> simplified = tokenSimplifier.simplifyTokens(tokens);

        // Then
        assertThat(simplified).hasSize(3);
        
        // OPEN_MARK avec attributs
        assertThat(simplified.get(0).getType()).isEqualTo(TOKEN_TYPE.OPEN_MARK);
        OpenMark openMark = simplified.get(0).getOpenMark();
        assertThat(openMark.getElementName()).isEqualTo("function");
        assertThat(openMark.getAttributes()).containsEntry("id", "230");
        
        // CONTENT
        assertThat(simplified.get(1).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(1).getStringValue()).isEqualTo("function d(){}");
        
        // CLOSE_MARK
        assertThat(simplified.get(2).getType()).isEqualTo(TOKEN_TYPE.CLOSE_MARK);
        CloseMark closeMark = simplified.get(2).getCloseMark();
        assertThat(closeMark.getElementName()).isEqualTo("function");
    }

    @Test
    void should_simplify_nested_elements() {
        // Given - Éléments imbriqués
        List<Token> tokens = Arrays.asList(
            // Outer element: main
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "before text"),
            
            // Inner element: function
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.STRING, " id"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "123"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "func content"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.SLASH, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            
            // Continue main content
            new Token(TOKEN_TYPE.STRING, "after text"),
            
            // Close main
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.SLASH, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Token> simplified = tokenSimplifier.simplifyTokens(tokens);

        // Then - Attendu: [OPEN_MARK(main)][CONTENT][OPEN_MARK(function)][CONTENT][CLOSE_MARK(function)][CONTENT][CLOSE_MARK(main)]
        assertThat(simplified).hasSize(7);
        
        // OPEN_MARK main
        assertThat(simplified.get(0).getType()).isEqualTo(TOKEN_TYPE.OPEN_MARK);
        assertThat(simplified.get(0).getOpenMark().getElementName()).isEqualTo("main");
        
        // CONTENT avant
        assertThat(simplified.get(1).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(1).getStringValue()).isEqualTo("before text");
        
        // OPEN_MARK function
        assertThat(simplified.get(2).getType()).isEqualTo(TOKEN_TYPE.OPEN_MARK);
        assertThat(simplified.get(2).getOpenMark().getElementName()).isEqualTo("function");
        assertThat(simplified.get(2).getOpenMark().getAttributes()).containsEntry("id", "123");
        
        // CONTENT function
        assertThat(simplified.get(3).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(3).getStringValue()).isEqualTo("func content");
        
        // CLOSE_MARK function
        assertThat(simplified.get(4).getType()).isEqualTo(TOKEN_TYPE.CLOSE_MARK);
        assertThat(simplified.get(4).getCloseMark().getElementName()).isEqualTo("function");
        
        // CONTENT après
        assertThat(simplified.get(5).getType()).isEqualTo(TOKEN_TYPE.CONTENT);
        assertThat(simplified.get(5).getStringValue()).isEqualTo("after text");
        
        // CLOSE_MARK main
        assertThat(simplified.get(6).getType()).isEqualTo(TOKEN_TYPE.CLOSE_MARK);
        assertThat(simplified.get(6).getCloseMark().getElementName()).isEqualTo("main");
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
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.SLASH, "/"),
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
