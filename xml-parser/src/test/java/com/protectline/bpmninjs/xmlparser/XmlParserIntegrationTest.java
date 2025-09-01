package com.protectline.bpmninjs.xmlparser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class XmlParserIntegrationTest {

    @Test
    void should_parse_complete_js_project_workflow() {
        // Given - JS Project avec balises commentées
        String jsContent = """
            //<main>
            console.log('Starting workflow');
            //<function id=1>
            function processData() {
                return 'processed';
            }
            //</function>
            console.log('Workflow complete');
            //</main>
            """;

        // When
        Lexer lexer = LexerFactory.createJsProjectLexer();
        List<Token> tokens = lexer.tokenize(jsContent);
        TokenParser parser = new TokenParser();
        List<Element> elements = parser.parseTokensToElements(tokens);

        // Then
        assertThat(elements).hasSize(2);
        
        // Main element
        Element mainElement = elements.stream()
            .filter(e -> "main".equals(e.getElementName()))
            .findFirst()
            .orElseThrow();
            
        assertThat(mainElement.getAttributes()).isEmpty();
        assertThat(mainElement.getContent()).isEqualTo("\nconsole.log('Starting workflow');\n\nconsole.log('Workflow complete');\n");

        // Function element
        List<Element> functionElements = elements.stream()
            .filter(e -> "function".equals(e.getElementName()))
            .toList();

        assertThat(functionElements).hasSize(1);
        Element functionElement = functionElements.get(0);
        assertThat(functionElement.getAttributes()).containsExactly(
            entry("id", "1")
        );
        assertThat(functionElement.getContent()).contains("function processData()");
    }

    @Test
    void should_parse_complete_bpmn_workflow() {
        // Given - BPMN XML standard
        String bpmnContent = """
            <bpmn:process id="Process_1">
            <bpmn:task id="task1" name="Process Data">
                <bpmn:documentation>Process the input data</bpmn:documentation>
            </bpmn:task>
            </bpmn:process>
            """;

        // When
        Lexer lexer = LexerFactory.createBpmnLexer();
        List<Token> tokens = lexer.tokenize(bpmnContent);
        TokenParser parser = new TokenParser();
        List<Element> elements = parser.parseTokensToElements(tokens);

        // Then
        assertThat(elements).hasSize(3); // process, startEvent, task, endEvent
        
        // Process element
        Element processElement = elements.stream()
            .filter(e -> "bpmn:process".equals(e.getElementName()))
            .findFirst()
            .orElseThrow();
            
        assertThat(processElement.getAttributes()).containsExactly(
            entry("id", "\"Process_1\"")
        );
        
        // Task element
        Element taskElement = elements.stream()
            .filter(e -> "bpmn:task".equals(e.getElementName()))
            .findFirst()
            .orElseThrow();
            
        assertThat(taskElement.getAttributes()).containsExactly(
            entry("name", "\"Process Data\""),
            entry("id", "\"task1\"")
        );
    }

    @Test
    void should_handle_mixed_formats_with_different_lexers() {
        // Given
        String jsFormat = "//<function>JS Content//</function>";
        String bpmnFormat = "<task>BPMN Content</task>";

        // When
        List<Element> jsElements = parseContent(jsFormat, LexerFactory.createJsProjectLexer());
        List<Element> bpmnElements = parseContent(bpmnFormat, LexerFactory.createBpmnLexer());

        // Then
        assertThat(jsElements).hasSize(1);
        assertThat(jsElements.get(0).getElementName()).isEqualTo("function");
        assertThat(jsElements.get(0).getContent()).isEqualTo("JS Content");

        assertThat(bpmnElements).hasSize(1);
        assertThat(bpmnElements.get(0).getElementName()).isEqualTo("task");
        assertThat(bpmnElements.get(0).getContent()).isEqualTo("BPMN Content");
    }

    private List<Element> parseContent(String content, Lexer lexer) {
        List<Token> tokens = lexer.tokenize(content);
        TokenParser parser = new TokenParser();
        return parser.parseTokensToElements(tokens);
    }
}
