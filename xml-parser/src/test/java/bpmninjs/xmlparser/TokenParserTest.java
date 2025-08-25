package bpmninjs.xmlparser;

import com.protectline.bpmninjs.xmlparser.Element;
import com.protectline.bpmninjs.xmlparser.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.Token;
import com.protectline.bpmninjs.xmlparser.TokenParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenParserTest {

    private TokenParser tokenParser;

    @BeforeEach
    void setUp() {
        tokenParser = new TokenParser();
    }

    @Test
    void should_parse_simple_element() {
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
        List<Element> elements = tokenParser.parseTokensToElements(tokens);

        // Then
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        assertThat(element.getElementName()).isEqualTo("function");
        assertThat(element.getAttributes()).containsEntry("id", "230");
        assertThat(element.getContent()).isEqualTo("function d(){}");
    }

    @Test
    void should_parse_element_without_attributes() {
        // Given
        List<Token> tokens = Arrays.asList(
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "some content"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"),
            new Token(TOKEN_TYPE.SLASH, "/"),
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
    void should_parse_nested_elements() {
        // Given - Test case: [OPEN_MARK][CONTENT][OPEN_MARK][CONTENT][CLOSE_MARK][CONTENT][CLOSE_MARK]
        // Représente: //<main>before text//<function id=123>func content</function/>after text</main/>
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
        List<Element> elements = tokenParser.parseTokensToElements(tokens);

        // Then - On devrait avoir 2 éléments : main et function (ordre pas important)
        assertThat(elements).hasSize(2);
        
        // Créer les éléments expected
        Element expectedMainElement = new Element("main", new HashMap<>(), "before textafter text");
        
        Map<String, String> functionAttributes = new HashMap<>();
        functionAttributes.put("id", "123");
        Element expectedFunctionElement = new Element("function", functionAttributes, "func content");
        
        // Vérifier que les éléments actuels contiennent les éléments attendus (peu importe l'ordre)
        assertThat(elements).usingRecursiveFieldByFieldElementComparator()
                           .containsExactlyInAnyOrder(expectedMainElement, expectedFunctionElement);
    }

    @Test
    void should_validate_matching_open_close_marks() {
        // Given - Balises non correspondantes mais format correct pour atteindre la validation
        List<Token> tokens = Arrays.asList(
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content"),
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
            new Token(TOKEN_TYPE.ELEMENT, "main"), // Nom différent !
            new Token(TOKEN_TYPE.SLASH, "/"),
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
            new Token(TOKEN_TYPE.SLASH_SLASH, "//"),
            new Token(TOKEN_TYPE.OPEN, "<"),
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
