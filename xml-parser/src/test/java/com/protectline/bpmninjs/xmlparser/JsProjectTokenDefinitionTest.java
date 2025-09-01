package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsProjectTokenDefinitionTest {

    private JsProjectTokenDefinition tokenDefinition;

    @BeforeEach
    void setUp() {
        tokenDefinition = new JsProjectTokenDefinition();
    }

    @Test
    void should_match_open_token() {
        // Given
        String content = "//<function";
        
        // When & Then
        assertThat(tokenDefinition.matches(content, 0, TOKEN_TYPE.OPEN)).isTrue();
        assertThat(tokenDefinition.getTokenLength(TOKEN_TYPE.OPEN)).isEqualTo(3);
    }

    @Test
    void should_not_match_incomplete_open_token() {
        // Given
        String content = "//function"; // Pas de <
        
        // When & Then
        assertThat(tokenDefinition.matches(content, 0, TOKEN_TYPE.OPEN)).isFalse();
    }

    @Test
    void should_match_close_token() {
        // Given
        String content = ">content";
        
        // When & Then
        assertThat(tokenDefinition.matches(content, 0, TOKEN_TYPE.CLOSE)).isTrue();
        assertThat(tokenDefinition.getTokenLength(TOKEN_TYPE.CLOSE)).isEqualTo(1);
    }

    @Test
    void should_match_end_symbol_token() {
        // Given
        String content = "/>";
        
        // When & Then
        assertThat(tokenDefinition.matches(content, 0, TOKEN_TYPE.END_SYMBOL)).isTrue();
        assertThat(tokenDefinition.getTokenLength(TOKEN_TYPE.END_SYMBOL)).isEqualTo(1);
    }

    @Test
    void should_match_equals_token() {
        // Given
        String content = "=value";
        
        // When & Then
        assertThat(tokenDefinition.matches(content, 0, TOKEN_TYPE.EQUALS)).isTrue();
        assertThat(tokenDefinition.getTokenLength(TOKEN_TYPE.EQUALS)).isEqualTo(1);
    }
}