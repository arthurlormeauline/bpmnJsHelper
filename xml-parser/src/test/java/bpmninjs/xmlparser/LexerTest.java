package bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.Lexer;
import com.protectline.bpmninjs.xmlparser.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LexerTest {

    private Lexer lexer;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
    }

    @Test
    void should_tokenize_simple_function_tag() {
        // Given
        String input = "//<function id=230>function d(){}<function/>";

        // When
        List<Token> tokens = lexer.tokenize(input);

        // Then
        assertThat(tokens).containsExactly(
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.STRING, " id"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "230"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "function d(){}"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.SLASH, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
    }

    @Test
    void should_tokenize_with_spaces_and_newlines() {
        // Given
        String input = "//<main>\n\nsome content\n//<main/>";

        // When
        List<Token> tokens = lexer.tokenize(input);

        // Then
        assertThat(tokens).containsExactly(
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "\n\nsome content\n"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.SLASH, "/"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );
    }
}
