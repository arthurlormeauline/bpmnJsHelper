package com.protectline.bpmninjs.xmlparser.parser;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OpenMarkTest {
    
    @Test
    void should_create_open_mark_with_element_name_and_attributes() {
        // Given
        String elementName = "bpmn:task";
        Map<String, String> attributes = Map.of("id", "task1", "name", "My Task");
        
        // When
        OpenMark openMark = new OpenMark(elementName, attributes);
        
        // Then
        assertThat(openMark.getElementName()).isEqualTo("bpmn:task");
        assertThat(openMark.getAttributes()).containsExactlyInAnyOrderEntriesOf(attributes);
    }
    
    @Test
    void should_handle_empty_attributes() {
        // Given
        String elementName = "element";
        Map<String, String> attributes = Map.of();
        
        // When
        OpenMark openMark = new OpenMark(elementName, attributes);
        
        // Then
        assertThat(openMark.getElementName()).isEqualTo("element");
        assertThat(openMark.getAttributes()).isEmpty();
    }
    
    @Test
    void should_implement_equals_correctly() {
        // Given
        OpenMark mark1 = new OpenMark("test", Map.of("attr", "value"));
        OpenMark mark2 = new OpenMark("test", Map.of("attr", "value"));
        OpenMark mark3 = new OpenMark("other", Map.of("attr", "value"));
        
        // When & Then
        assertThat(mark1).isEqualTo(mark2);
        assertThat(mark1).isNotEqualTo(mark3);
        assertThat(mark1.hashCode()).isEqualTo(mark2.hashCode());
    }
}