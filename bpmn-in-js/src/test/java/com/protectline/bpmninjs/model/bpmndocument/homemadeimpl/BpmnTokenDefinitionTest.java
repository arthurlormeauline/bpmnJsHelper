package com.protectline.bpmninjs.model.bpmndocument.homemadeimpl;

import com.protectline.bpmninjs.xmlparser.lexer.TOKEN_TYPE;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BpmnTokenDefinitionTest {
    
    @Test
    void should_define_bpmn_open_symbol() {
        // Given
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        
        // When
        String openSymbol = tokenDefinition.getTypeValues(TOKEN_TYPE.OPEN).get(0);
        
        // Then
        assertThat(openSymbol).isEqualTo("<");
    }
    
    @Test 
    void should_define_bpmn_close_symbol() {
        // Given
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        
        // When
        String closeSymbol = tokenDefinition.getTypeValues(TOKEN_TYPE.CLOSE).get(0);
        
        // Then
        assertThat(closeSymbol).isEqualTo(">");
    }
    
    @Test
    void should_recognize_xml_declaration() {
        // Given
        BpmnTokenDefinition tokenDefinition = new BpmnTokenDefinition();
        
        // When
        TOKEN_TYPE type = tokenDefinition.getType("<?");
        
        // Then  
        assertThat(type).isEqualTo(TOKEN_TYPE.OPEN);
    }
}
