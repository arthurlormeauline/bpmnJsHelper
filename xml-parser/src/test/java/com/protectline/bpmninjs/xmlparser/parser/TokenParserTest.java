package com.protectline.bpmninjs.xmlparser.parser;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import com.protectline.bpmninjs.xmlparser.lexer.Token;
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
            new Token(TOKEN_TYPE.STRING, "function id"),
            new Token(TOKEN_TYPE.EQUALS, "="),
            new Token(TOKEN_TYPE.STRING, "230"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "function d(){}"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.STRING, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);

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
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);

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
            new Token(TOKEN_TYPE.STRING, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "some content"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),

            new Token(TOKEN_TYPE.STRING, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);

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
            new Token(TOKEN_TYPE.STRING, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
            new Token(TOKEN_TYPE.STRING, "main"),
            new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When & Then
        assertThatThrownBy(() -> tokenParser.parseXmlAndGetElements(tokens))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Mismatched tags: function != main");
    }
    
    @Test
    void should_detect_missing_close_mark() {
        // Given - Balise fermante manquante
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
            new Token(TOKEN_TYPE.STRING, "function"),
            new Token(TOKEN_TYPE.CLOSE, ">"),
            new Token(TOKEN_TYPE.STRING, "content")
            // Pas de balise fermante !
        );

        // When & Then
        assertThatThrownBy(() -> tokenParser.parseXmlAndGetElements(tokens))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Missing CLOSE_MARK for element: function");
    }
    
    // Tests from TokenParserTreeTest - Tree building functionality
    @Test
    void should_parse_single_self_closing_element() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("element", java.util.Map.of("attr", "value")))
        );

        // When
        Element root = tokenParser.parseXml(tokens);
        
        // Then
        assertThat(root).isNotNull();
        assertThat(root.getElementName()).isEqualTo("element");
        assertThat(root.getChildren()).isEmpty();
        assertThat(root.isSelfClosing()).isTrue();
        assertThat(root.getAttributes()).containsEntry("attr", "value");
    }

    @Test 
    void should_parse_element_with_content() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("element", java.util.Map.of("attr", "value"))),
            new Token(TOKEN_TYPE.CONTENT, "content"),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("element"))
        );

        // When
        Element root = tokenParser.parseXml(tokens);
        
        // Then
        assertThat(root).isNotNull();
        assertThat(root.getElementName()).isEqualTo("element");
        assertThat(root.getChildren()).isEmpty();
        assertThat(root.isSelfClosing()).isFalse();
        assertThat(root.getContent()).isEqualTo("content");
        assertThat(root.getAttributes()).containsEntry("attr", "value");
    }

    @Test
    void should_parse_nested_elements() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("parent", java.util.Map.of())),
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("child", java.util.Map.of())),
            new Token(TOKEN_TYPE.CONTENT, "child content"),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("child")),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("parent"))
        );

        // When
        Element root = tokenParser.parseXml(tokens);
        
        // Then
        assertThat(root).isNotNull();
        assertThat(root.getElementName()).isEqualTo("parent");
        assertThat(root.getChildren()).hasSize(1);
        
        Element child = root.getChildren().get(0);
        assertThat(child.getElementName()).isEqualTo("child");
        assertThat(child.getContent()).isEqualTo("child content");
    }

    @Test
    void should_parse_mixed_content() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("parent", java.util.Map.of())),
            new Token(TOKEN_TYPE.CONTENT, "text before"),
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("selfClosing", java.util.Map.of())),
            new Token(TOKEN_TYPE.CONTENT, "text after"),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("parent"))
        );

        // When
        Element root = tokenParser.parseXml(tokens);
        
        // Then
        assertThat(root).isNotNull();
        assertThat(root.getElementName()).isEqualTo("parent");
        assertThat(root.getChildren()).hasSize(1);
        
        Element selfClosingChild = root.getChildren().get(0);
        assertThat(selfClosingChild.getElementName()).isEqualTo("selfClosing");
        assertThat(selfClosingChild.isSelfClosing()).isTrue();
    }

    @Test
    void should_parse_multiple_top_level_elements() {
        // Given
        List<Token> tokens = List.of(
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("first", java.util.Map.of())),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("first")),
            new Token(TOKEN_TYPE.SELF_CLOSE_MARK, 
                new SelfCloseMark("second", java.util.Map.of())),
            new Token(TOKEN_TYPE.OPEN_MARK, 
                new OpenMark("third", java.util.Map.of())),
            new Token(TOKEN_TYPE.CLOSE_MARK, 
                new CloseMark("third"))
        );

        // When
        Element root = tokenParser.parseXml(tokens);
        List<Element> allElements = tokenParser.parseXmlAndGetElements(tokens);
        
        // Then
        assertThat(root).isNotNull();
        assertThat(root.getElementName()).isEqualTo("first");
        assertThat(root.getChildren()).isEmpty();
        
        assertThat(allElements).hasSize(3);
        assertThat(allElements.get(0).getElementName()).isEqualTo("first");
        assertThat(allElements.get(1).getElementName()).isEqualTo("second");
        assertThat(allElements.get(2).getElementName()).isEqualTo("third");
    }
}
