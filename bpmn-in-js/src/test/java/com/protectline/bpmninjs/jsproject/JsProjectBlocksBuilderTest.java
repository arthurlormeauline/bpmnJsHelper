package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.blocksfactory.JsProjectBlocksBuilder;
import com.protectline.bpmninjs.jsproject.blocksfactory.blockbuilder.BlockFromElementFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectBlocksBuilderTest {

    JsProjectBlocksBuilder jsProjectBlocksBuilder;

    @BeforeEach
    void setup() throws IOException, URISyntaxException {
        FileUtil files = new FileUtil(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        jsProjectBlocksBuilder = new JsProjectBlocksBuilder(new BlockFromElementFactory(files));
    }


    @Test
    void should_parse_js_file() throws URISyntaxException, IOException {
        // Given
        FileUtil files = new FileUtil(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        var content = files.getJsRunnerFileContent("tus.prc.actionCombine");

        // When
        var blocksFromProject = jsProjectBlocksBuilder.parseJsToBlocks(content);

        // Then
        assertThat(blocksFromProject.size()).isEqualTo(20);
    }

    @Test
    void should_parse_minimal_two_function_js_content() throws URISyntaxException, IOException {
        // Given - Read minimal JS content from file
        FileUtil files = new FileUtil(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        var content = files.getJsRunnerFileContent("CreateCustomer_Dev_minimal");


        // When - Parse the JS content directly
        var blocksFromProject = jsProjectBlocksBuilder.parseJsToBlocks(content);

        // Then - Should parse 2 functions but currently only parses 1
        System.out.println("=== TEST RESULT ===");
        System.out.println("Expected blocks: 2");
        System.out.println("Actual blocks: " + blocksFromProject.size());
        for (int i = 0; i < blocksFromProject.size(); i++) {
            var block = blocksFromProject.get(i);
            System.out.println("Block " + i + ": " + block.getName() + " (ID: " + block.getId() + ")");
        }
        
        assertThat(blocksFromProject.size()).isEqualTo(2);
    }

}
