package com.protectline.bpmninjs.jsproject.jsupdater;

import com.protectline.bpmninjs.bpmndocument.model.BpmnPath;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.jsproject.updatertemplate.JsUpdaterTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionUpdaterTest {

    private FunctionUpdater functionUpdater;
    private JsUpdaterTemplate template;

    @BeforeEach
    void setUp() {
        template = new JsUpdaterTemplate("FUNCTION", "//<function id=**id**>\n**name**() {\n**content**\n}\n//</function>\n\n", "//<FUNCTIONS>");
        functionUpdater = new FunctionUpdater(template);
    }

    @Test
    void should_normalize_function_name_with_special_characters() {
        // Given
        BpmnPath path = new BpmnPath("Activity_1sbsavb");
        String originalName = "check if serviceInstance mandatory = false";
        Block block = new Block(path, originalName + "_0", "some content", NodeType.SCRIPT, "Activity_1sbsavb");
        
        // When
        String result = functionUpdater.update("//<FUNCTIONS>", List.of(block));
        
        // Then
        assertThat(result).contains("check_if_serviceInstance_mandatory_false_0() {");
        assertThat(result).doesNotContain("check if serviceInstance mandatory = false_0() {");
    }

    @Test
    void should_normalize_function_name_with_accents() {
        // Given
        BpmnPath path = new BpmnPath("Activity_test");
        String originalName = "créer élément avec accénts";
        Block block = new Block(path, originalName + "_0", "some content", NodeType.SCRIPT, "Activity_test");
        
        // When
        String result = functionUpdater.update("//<FUNCTIONS>", List.of(block));
        
        // Then
        assertThat(result).contains("creer_element_avec_accents_0() {");
        assertThat(result).doesNotContain("créer élément avec accénts_0() {");
    }

    @Test
    void should_handle_name_starting_with_number() {
        // Given
        BpmnPath path = new BpmnPath("Activity_test");
        String originalName = "123 start with number";
        Block block = new Block(path, originalName + "_0", "some content", NodeType.SCRIPT, "Activity_test");
        
        // When
        String result = functionUpdater.update("//<FUNCTIONS>", List.of(block));
        
        // Then
        assertThat(result).contains("func_123_start_with_number_0() {");
    }

    @Test
    void should_handle_empty_name() {
        // Given
        BpmnPath path = new BpmnPath("Activity_test");
        String originalName = "";
        Block block = new Block(path, originalName + "_0", "some content", NodeType.SCRIPT, "Activity_test");
        
        // When
        String result = functionUpdater.update("//<FUNCTIONS>", List.of(block));
        
        // Then - Le nom vide + "_0" devient "func_0" après normalisation
        assertThat(result).contains("func_0() {");
    }
}
