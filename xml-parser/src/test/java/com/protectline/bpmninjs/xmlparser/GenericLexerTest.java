package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenericLexerTest {

    @Test
    void should_tokenize_js_project_simple_function_tag() {
        // Given
        Lexer lexer = new Lexer(new JsProjectTokenDefinition());
        String input = "//<function id=230>function d(){}//</function>";

        // When
        List<Token> tokens = lexer.tokenize(input);

        // Then
        assertThat(tokens).containsExactly(
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
    }

    @Test
    void should_tokenize_bpmn_simple_task() {
        // Given
        Lexer lexer = new Lexer(new BpmnTokenDefinition());
        String input = "<bpmn:task id=\"task1\">Some content</bpmn:task>";

        // When
        List<Token> tokens = lexer.tokenize(input);

        // Then
        assertThat(tokens).containsExactly(
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "bpmn:task"),
            new Token(TOKEN_TYPE.STRING, " id"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "\"task1\""),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "Some content"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.ELEMENT, "bpmn:task"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
    }

    @Test
    void should_tokenize_js_project_with_spaces_and_newlines() {
        // Given
        Lexer lexer = new Lexer(new JsProjectTokenDefinition());
        String input = "//<main>\n\nsome content\n//</main>";

        // When
        List<Token> tokens = lexer.tokenize(input);

        // Then
        assertThat(tokens).containsExactly(
            new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "\n\nsome content\n"),
            new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
    }

    @Test
    void should_use_factory_methods() {
        // Given
        String jsInput = "//<function>content//</function>";
        String bpmnInput = "<task>content</task>";

        // When
        List<Token> jsTokens = LexerFactory.createJsProjectLexer().tokenize(jsInput);
        List<Token> bpmnTokens = LexerFactory.createBpmnLexer().tokenize(bpmnInput);

        // Then
        assertThat(jsTokens).hasSize(8);
        assertThat(jsTokens.get(0)).isEqualTo(new Token(TOKEN_TYPE.OPEN, "//<"));
        
        assertThat(bpmnTokens).hasSize(8);
        assertThat(bpmnTokens.get(0)).isEqualTo(new Token(TOKEN_TYPE.OPEN, "<"));
    }
}
