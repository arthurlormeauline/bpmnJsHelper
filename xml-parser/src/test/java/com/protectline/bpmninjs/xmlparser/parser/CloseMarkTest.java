package com.protectline.bpmninjs.xmlparser.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CloseMarkTest {
    
    @Test
    void should_create_close_mark_with_element_name() {
        // Given
        String elementName = "bpmn:task";
        
        // When
        CloseMark closeMark = new CloseMark(elementName);
        
        // Then
        assertThat(closeMark.getElementName()).isEqualTo("bpmn:task");
    }
    
    @Test
    void should_implement_equals_correctly() {
        // Given
        CloseMark mark1 = new CloseMark("test");
        CloseMark mark2 = new CloseMark("test");
        CloseMark mark3 = new CloseMark("other");
        
        // When & Then
        assertThat(mark1).isEqualTo(mark2);
        assertThat(mark1).isNotEqualTo(mark3);
        assertThat(mark1.hashCode()).isEqualTo(mark2.hashCode());
    }
    
    @Test
    void should_have_meaningful_toString() {
        // Given
        CloseMark closeMark = new CloseMark("element");
        
        // When
        String toString = closeMark.toString();
        
        // Then
        assertThat(toString).contains("CloseMark");
        assertThat(toString).contains("element");
    }
}