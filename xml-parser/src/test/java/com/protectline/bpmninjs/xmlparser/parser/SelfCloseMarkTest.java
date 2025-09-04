package com.protectline.bpmninjs.xmlparser.parser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SelfCloseMarkTest {

    @Test
    void should_create_self_closing_mark_with_element_name_and_attributes() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("id", "test");
        attributes.put("name", "testName");
        
        // When
        SelfCloseMark selfCloseMark = new SelfCloseMark("element", attributes);
        
        // Then
        assertThat(selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(selfCloseMark.getAttributes()).isEqualTo(attributes);
        assertThat(selfCloseMark.getAttributes()).containsEntry("id", "test");
        assertThat(selfCloseMark.getAttributes()).containsEntry("name", "testName");
    }

    @Test
    void should_have_meaningful_toString_representation() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "value");
        
        // When
        SelfCloseMark selfCloseMark = new SelfCloseMark("test", attributes);
        String stringRepr = selfCloseMark.toString();
        
        // Then
        assertThat(stringRepr).contains("SelfCloseMark");
        assertThat(stringRepr).contains("test");
        assertThat(stringRepr).contains("attr");
        assertThat(stringRepr).contains("value");
    }

    @Test
    void should_handle_empty_attributes() {
        // Given
        Map<String, String> emptyAttributes = new HashMap<>();
        
        // When
        SelfCloseMark selfCloseMark = new SelfCloseMark("element", emptyAttributes);
        
        // Then
        assertThat(selfCloseMark.getElementName()).isEqualTo("element");
        assertThat(selfCloseMark.getAttributes()).isEmpty();
    }
}