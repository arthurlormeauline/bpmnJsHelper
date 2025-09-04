package com.protectline.bpmninjs.xmlparser.lexer;

import com.protectline.bpmninjs.xmlparser.util.LexerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE.*;
import static org.assertj.core.api.Assertions.assertThat;

class LexerTest {

    @Test
    void should_handle_url_in_attribute() {
        // Given
        String input = "//<function id=\"https://test\">function d(){}//</function>";

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(input);

        // Then
        assertThat(tokens).containsExactly(
                new Token(OPEN, "//<"),
                new Token(STRING, "function id"),
                new Token(EQUALS, "="),
                new Token(QUOTE, "\""),
                new Token(STRING, "https:"),
                new Token(END_SYMBOL, "/"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "test"),
                new Token(QUOTE, "\""),
                new Token(CLOSE, ">"),
                new Token(STRING, "function d(){}"),
                new Token(OPEN, "//<"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "function"),
                new Token(CLOSE, ">")
        );
    }
    @Test
    void should_tokenize_js_project_simple_function_tag() {
        // Given
        String input = "//<function id=230>function d(){}//</function>";

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(input);

        // Then
        assertThat(tokens).containsExactly(
            new Token(OPEN, "//<"),
            new Token(STRING, "function id"),
            new Token(EQUALS, "="),
            new Token(STRING, "230"),
            new Token(CLOSE, ">"),
            new Token(STRING, "function d(){}"),
            new Token(OPEN, "//<"),
            new Token(END_SYMBOL, "/"),
            new Token(STRING, "function"),
            new Token(CLOSE, ">")
        );
    }

    @Test
    void should_tokenize_bpmn_simple_task() {
        // Given
        String input = "<bpmn:task id=\"task1\">Some content</bpmn:task>";

        // When
        List<Token> tokens = LexerFactory.tokenizeBpmn(input);

        // Then
        assertThat(tokens).containsExactly(
            new Token(OPEN, "<"),
            new Token(STRING, "bpmn:task id"),
            new Token(EQUALS, "="),
            new Token(QUOTE, "\""),
            new Token(STRING, "task1"),
            new Token(QUOTE, "\""),
            new Token(CLOSE, ">"),
            new Token(STRING, "Some content"),
            new Token(OPEN, "<"),
            new Token(END_SYMBOL, "/"),
            new Token(STRING, "bpmn:task"),
            new Token(CLOSE, ">")
        );
    }

    @Test
    void should_tokenize_js_project_with_spaces_and_newlines() {
        // Given
        String input = "//<main>\n\nsome content\n//</main>";

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(input);

        // Then
        assertThat(tokens).containsExactly(
                new Token(OPEN, "//<"),
            new Token(STRING, "main"),
            new Token(CLOSE, ">"),
            new Token(STRING, "\n\nsome content\n"),
                new Token(OPEN, "//<"),
            new Token(END_SYMBOL, "/"),
            new Token(STRING, "main"),
            new Token(CLOSE, ">")
        );
    }

    @Test
    void should_use_factory_methods() {
        // Given
        String jsInput = "//<function>content//</function>";
        String bpmnInput = "<task>content</task>";

        // When
        List<Token> jsTokens = LexerFactory.tokenizeJsProject(jsInput);
        List<Token> bpmnTokens = LexerFactory.tokenizeBpmn(bpmnInput);

        // Then
        assertThat(jsTokens).hasSize(8);
        assertThat(jsTokens.get(0)).isEqualTo(new Token(OPEN, "//<"));
        
        assertThat(bpmnTokens).hasSize(8);
        assertThat(bpmnTokens.get(0)).isEqualTo(new Token(OPEN, "<"));
    }

    @Test
    void should_tokenize_real_js_file_content() throws Exception {
        // Given
        var resourcePath = this.getClass().getClassLoader().getResourceAsStream("tobpmn/output/CreateCustomer_Dev_minimal/BpmnRunner.js");

        String jsContent;
        if (resourcePath != null) {
            jsContent = new String(resourcePath.readAllBytes());
        } else {
            // Final fallback to our created file
            jsContent = java.nio.file.Files.readString(java.nio.file.Paths.get("minimal_test_content.js"));
        }

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(jsContent);

        // Then
        assertThat(tokens).isNotEmpty();
        assertThat(tokens.stream().anyMatch(token -> token.getType() == OPEN)).isTrue();
        assertThat(tokens.stream().anyMatch(token -> token.getType() == STRING)).isTrue();
    }


    @Test
    void should_handle_apostrophe_in_js_content() throws Exception {
        // Given
        var resourcePath = this.getClass().getClassLoader().getResourceAsStream("apostrophe_test.js");
        String jsContent;
        if (resourcePath != null) {
            jsContent = new String(resourcePath.readAllBytes());
        } else {
            jsContent = java.nio.file.Files.readString(java.nio.file.Paths.get("apostrophe_test.js"));
        }

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(jsContent);

        // Then
        assertThat(tokens).hasSize(5);
        assertThat(tokens.stream().anyMatch(token -> token.getValue().contains("'"))).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideWordTokenTestCases")
    void should_parse_words_with_special_tokens(String input, List<Token> expectedTokens) {
        // Given

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(input);

        // Then
        assertThat(tokens).containsExactly(expectedTokens.toArray(new Token[0]));
    }
    
    static Stream<Arguments> provideWordTokenTestCases() {
        return Stream.of(
//            // Cas simples
            Arguments.of("un/test", Arrays.asList(
                new Token(STRING, "un"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "test")
            )),

            Arguments.of("un/autre=test", Arrays.asList(
                new Token(STRING, "un"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "autre"),
                new Token(EQUALS, "="),
                new Token(STRING, "test")
            )),

            // Test pour le bug : tokens spéciaux après des caractères normaux puis d'autres tokens spéciaux
            Arguments.of("start/middle=end/final", Arrays.asList(
                new Token(STRING, "start"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "middle"),
                new Token(EQUALS, "="),
                new Token(STRING, "end"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "final")
            )),

            Arguments.of("/test", Arrays.asList(
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "test")
            )),

            Arguments.of("test/", Arrays.asList(
                new Token(STRING, "test"),
                new Token(END_SYMBOL, "/")
            )),

            Arguments.of("avant/milieu/après", Arrays.asList(
                new Token(STRING, "avant"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "milieu"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "après")
            )),

            Arguments.of("var=value", Arrays.asList(
                new Token(STRING, "var"),
                new Token(EQUALS, "="),
                new Token(STRING, "value")
            )),

            Arguments.of("//<function", Arrays.asList(
                new Token(OPEN, "//<"),
                new Token(STRING, "function")
            )),

            Arguments.of("id=abc123>", Arrays.asList(
                new Token(STRING, "id"),
                new Token(EQUALS, "="),
                new Token(STRING, "abc123"),
                new Token(CLOSE, ">")
            )),

            Arguments.of("a=b=c", Arrays.asList(
                new Token(STRING, "a"),
                new Token(EQUALS, "="),
                new Token(STRING, "b"),
                new Token(EQUALS, "="),
                new Token(STRING, "c")
            )),

            Arguments.of("path//double", Arrays.asList(
                new Token(STRING, "path"),
                new Token(END_SYMBOL, "/"),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "double")
            )),

            Arguments.of(">close", Arrays.asList(
                new Token(CLOSE, ">"),
                new Token(STRING, "close")
            )),

            Arguments.of("mix/>=/test", Arrays.asList(
                new Token(STRING, "mix"),
                new Token(END_SYMBOL, "/"),
                new Token(CLOSE, ">"),
                new Token(EQUALS, "="),
                new Token(END_SYMBOL, "/"),
                new Token(STRING, "test")
            ))
        );
    }

    @Test
    void should_tokenize_multiple_consecutive_url_attributes() {
        // Given - Input similar to real BPMN with multiple URL attributes
        String input = "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\">";

        // When
        List<Token> tokens = LexerFactory.tokenizeBpmn(input);
        
        // Debug
        debugTokenList("Multiple URL attributes", tokens);

        // Then - Exhaustive assertions
        assertThat(tokens).hasSize(21); // Exact expected size
        
        // Structure: OPEN + element_name + (EQUALS + QUOTE + url_parts + QUOTE) * 2 + CLOSE
        assertThat(tokens.get(0)).isEqualTo(new Token(OPEN, "<"));
        assertThat(tokens.get(1).getType()).isEqualTo(STRING);
        assertThat(tokens.get(1).getValue()).contains("bpmn:definitions");
        assertThat(tokens.get(1).getValue()).contains("xmlns:bpmn");
        assertThat(tokens.get(1).getValue()).contains("xmlns:bpmndi");
        
        // First URL: xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"
        assertThat(tokens.get(2)).isEqualTo(new Token(EQUALS, "="));
        assertThat(tokens.get(3)).isEqualTo(new Token(QUOTE, "\""));
        assertThat(tokens.get(4)).isEqualTo(new Token(STRING, "http:"));
        assertThat(tokens.get(5)).isEqualTo(new Token(END_SYMBOL, "/"));
        assertThat(tokens.get(6)).isEqualTo(new Token(END_SYMBOL, "/"));
        assertThat(tokens.get(7)).isEqualTo(new Token(STRING, "www.omg.org/spec/BPMN/20100524/MODEL"));
        assertThat(tokens.get(8)).isEqualTo(new Token(QUOTE, "\""));
        
        // Space + next attribute name
        assertThat(tokens.get(9).getType()).isEqualTo(STRING);
        assertThat(tokens.get(9).getValue()).isEqualTo(" xmlns:bpmndi");
        
        // Second URL: xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
        assertThat(tokens.get(10)).isEqualTo(new Token(EQUALS, "="));
        assertThat(tokens.get(11)).isEqualTo(new Token(QUOTE, "\""));
        assertThat(tokens.get(12)).isEqualTo(new Token(STRING, "http:"));
        assertThat(tokens.get(13)).isEqualTo(new Token(END_SYMBOL, "/"));
        assertThat(tokens.get(14)).isEqualTo(new Token(END_SYMBOL, "/"));
        assertThat(tokens.get(15)).isEqualTo(new Token(STRING, "www.omg.org/spec/BPMN/20100524/DI"));
        assertThat(tokens.get(16)).isEqualTo(new Token(QUOTE, "\""));
        
        // Closing
        assertThat(tokens.get(17)).isEqualTo(new Token(CLOSE, ">"));
        
        // Verify counts
        long quoteCount = tokens.stream().filter(t -> t.getType() == QUOTE).count();
        assertThat(quoteCount).isEqualTo(4);
        
        long slashCount = tokens.stream().filter(t -> t.getType() == END_SYMBOL).count();
        assertThat(slashCount).isEqualTo(4);
        
        long equalsCount = tokens.stream().filter(t -> t.getType() == EQUALS).count();
        assertThat(equalsCount).isEqualTo(2);
    }

    private static void debugTokenList(String testName, List<Token> tokens) {
        System.out.println("=== TOKENIZATION DEBUG: " + testName + " ===");
        System.out.println("Total tokens: " + tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            System.out.printf("%2d: %s%n", i, tokens.get(i));
        }
        System.out.println("=== END DEBUG ===");
    }

}
