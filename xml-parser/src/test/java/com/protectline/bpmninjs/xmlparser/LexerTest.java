package com.protectline.bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.tokendefinition.BpmnTokenDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static com.protectline.bpmninjs.xmlparser.TOKEN_TYPE.*;

class LexerTest {

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
            new Token(STRING, "\"task1\""),
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
    void should_debug_tokenize_minimal_test_content() throws Exception {
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

        // Then - Log all tokens for debugging
        System.out.println("=== LEXER DEBUG: Found " + tokens.size() + " tokens ===");
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String value = token.getValue()
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t")
                    .replace("'", "\\'");
            System.out.println("Token " + i + ": " + token.getType() + " = '" + value + "' (length: " + token.getValue().length() + ")");
        }

        // Count function tokens specifically
        long functionTokens = tokens.stream()
                .filter(token -> token.getType() == STRING && "function".equals(token.getValue()))
                .count();
        System.out.println("Found " + functionTokens + " function element tokens");

        // Look for function IDs
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == STRING && "function".equals(token.getValue())) {
                // Look ahead for id attribute
                for (int j = i + 1; j < Math.min(i + 10, tokens.size()); j++) {
                    Token nextToken = tokens.get(j);
                    if (nextToken.getValue().contains("32cb633b-0bb2-4c27-89fd-8812410863c6") ||
                            nextToken.getValue().contains("f15c404f-979e-440d-bbf0-04c98580d03b")) {
                        System.out.println("Found function ID in token " + j + ": " + nextToken.getValue());
                    }
                }
            }
        }

        // Verify we have tokens (but don't assert specific count since we're debugging)
        assertThat(tokens.size()).isGreaterThan(0);
    }


    @Test
    void should_debug_apostrophe_issue() throws Exception {
        // Given
        var resourcePath = this.getClass().getClassLoader().getResourceAsStream("apostrophe_test.js");
        String jsContent;
        if (resourcePath != null) {
            jsContent = new String(resourcePath.readAllBytes());
        } else {
            jsContent = java.nio.file.Files.readString(java.nio.file.Paths.get("apostrophe_test.js"));
        }

        System.out.println("=== INPUT CONTENT ===");
        System.out.println("'" + jsContent.replace("\n", "\\n").replace("\r", "\\r") + "'");
        System.out.println("Content length: " + jsContent.length());

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(jsContent);

        // Then - Log all tokens for debugging
        System.out.println("=== APOSTROPHE LEXER DEBUG: Found " + tokens.size() + " tokens ===");
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String value = token.getValue()
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t")
                    .replace("'", "\\'");
            System.out.println("Token " + i + ": " + token.getType() + " = '" + value + "' (length: " + token.getValue().length() + ")");
        }

        // Verify we have tokens (ajusté pour le nouveau comportement)
        assertThat(tokens.size()).isEqualTo(5);
    }

    @ParameterizedTest
    @MethodSource("provideWordTokenTestCases")
    void should_parse_words_with_special_tokens(String input, List<Token> expectedTokens) {
        // Given

        // When
        List<Token> tokens = LexerFactory.tokenizeJsProject(input);

        // Then
        if (expectedTokens == null) {
            // Cas où on veut juste logger pour debug
            logTokens(input, tokens);
            assertThat(tokens.size()).isGreaterThan(0);
        } else {
            assertThat(tokens).containsExactly(expectedTokens.toArray(new Token[0]));
        }
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

    // Méthode utilitaire pour logger au besoin
    private void logTokens(String input, List<Token> tokens) {
        System.out.println("\n=== LOGGING TOKENS FOR: '" + input + "' ===");
        System.out.println("Found " + tokens.size() + " tokens:");
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String value = token.getValue()
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
            System.out.println("  Token " + i + ": " + token.getType() + " = '" + value + "'");
        }
        System.out.println("========================");
    }
}
