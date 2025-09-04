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

    @Test
    void should_reconstruct_url_attribute_from_tokenized_slashes() {
        // Given - Token sequence from LexerTest.should_handle_url_in_attribute
        // Input was: //<function id="https://test">function d(){}//</function>
        // Lexer correctly tokenizes URL with "/" as END_SYMBOL tokens and quotes as QUOTE tokens
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.STRING, "function id"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "https:"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "test"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "function d(){}"),
                new Token(TOKEN_TYPE.OPEN, "//<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "function"),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);

        // Then - Parser should reconstruct the complete URL in the attribute
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        assertThat(element.getElementName()).isEqualTo("function");
        assertThat(element.getAttributes()).containsEntry("id", "https://test");
        assertThat(element.getContent()).isEqualTo("function d(){}");
    }

    @Test
    void should_reconstruct_multiple_consecutive_url_attributes() {
        // Given - Real token sequence from LexerTest debug output (34 tokens total)
        // Input: <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI">
        List<Token> tokens = Arrays.asList(
                // Structure discovered from lexer debug
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.STRING, "bpmn:definitions xmlns:bpmn"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "http:"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "www.omg.org"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "spec"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "BPMN"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "20100524"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "MODEL"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, " xmlns:bpmndi"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "http:"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "www.omg.org"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "spec"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "BPMN"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "20100524"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "DI"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.CLOSE, ">"),
                // Content (simplified)
                new Token(TOKEN_TYPE.STRING, "content"),
                // Closing tag: </bpmn:definitions>
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "bpmn:definitions"),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);

        // Then - Exhaustive verification
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        
        // Element structure
        assertThat(element.getElementName()).isEqualTo("bpmn:definitions");
        assertThat(element.isSelfClosing()).isFalse();
        assertThat(element.getContent()).isEqualTo("content");
        assertThat(element.getChildren()).isEmpty();
        
        // Attributes - both URLs should be correctly reconstructed with all their slashes
        assertThat(element.getAttributes()).hasSize(2);
        assertThat(element.getAttributes()).containsEntry("xmlns:bpmn", "http://www.omg.org/spec/BPMN/20100524/MODEL");
        assertThat(element.getAttributes()).containsEntry("xmlns:bpmndi", "http://www.omg.org/spec/BPMN/20100524/DI");
        
        // Verify attribute order - xmlns:bpmn should appear before xmlns:bpmndi
        List<String> attributeKeys = new java.util.ArrayList<>(element.getAttributes().keySet());
        assertThat(attributeKeys).containsExactly("xmlns:bpmn", "xmlns:bpmndi")
                .withFailMessage("Les attributs devraient être dans l'ordre d'apparition dans le XML");
        
        // Verify exact URL reconstruction
        String bpmnUrl = element.getAttributes().get("xmlns:bpmn");
        assertThat(bpmnUrl).isEqualTo("http://www.omg.org/spec/BPMN/20100524/MODEL");
        
        String bpmndiUrl = element.getAttributes().get("xmlns:bpmndi");
        assertThat(bpmndiUrl).isEqualTo("http://www.omg.org/spec/BPMN/20100524/DI");
    }

    @Test
    void should_preserve_attribute_order_in_parsing() {
        // Given - Element with multiple attributes in specific order: id, name, type, version
        List<Token> tokens = Arrays.asList(
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.STRING, "element id"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "test123"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, " name"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "TestElement"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, " type"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "task"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, " version"),
                new Token(TOKEN_TYPE.EQUALS, "="),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.STRING, "1.0"),
                new Token(TOKEN_TYPE.QUOTE, "\""),
                new Token(TOKEN_TYPE.CLOSE, ">"),
                new Token(TOKEN_TYPE.STRING, "content"),
                new Token(TOKEN_TYPE.OPEN, "<"),
                new Token(TOKEN_TYPE.END_SYMBOL, "/"),
                new Token(TOKEN_TYPE.STRING, "element"),
                new Token(TOKEN_TYPE.CLOSE, ">")
        );

        // When
        List<Element> elements = tokenParser.parseXmlAndGetElements(tokens);

        // Then
        assertThat(elements).hasSize(1);
        Element element = elements.get(0);
        
        // Verify element structure
        assertThat(element.getElementName()).isEqualTo("element");
        assertThat(element.getContent()).isEqualTo("content");
        
        // Verify all attributes are parsed correctly
        assertThat(element.getAttributes()).hasSize(4);
        assertThat(element.getAttributes()).containsEntry("id", "test123");
        assertThat(element.getAttributes()).containsEntry("name", "TestElement");
        assertThat(element.getAttributes()).containsEntry("type", "task");
        assertThat(element.getAttributes()).containsEntry("version", "1.0");
        
        // Verify attribute order preservation - critical assertion
        List<String> actualAttributeOrder = new java.util.ArrayList<>(element.getAttributes().keySet());
        List<String> expectedAttributeOrder = Arrays.asList("id", "name", "type", "version");
        assertThat(actualAttributeOrder).containsExactlyElementsOf(expectedAttributeOrder)
                .withFailMessage("L'ordre des attributs doit être préservé tel qu'il apparaît dans le XML source");
    }
}
