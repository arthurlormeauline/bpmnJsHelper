package com.protectline.bpmninjs.jsprojectapi;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.model.jsproject.api.JsNode;
import com.protectline.bpmninjs.model.jsproject.api.JsProject;
import com.protectline.bpmninjs.model.jsproject.JsProjectImpl;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;
import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromJsElementResult;
import com.protectline.bpmninjs.model.block.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectBlocksBuilderTest {

    MainFactory mainFactory;
    FileService fileService;

    @BeforeEach
    void setup() throws IOException, URISyntaxException {
        fileService = new FileService(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        mainFactory = MainFactoryTestUtil.createWithDefaults(fileService);
    }


    @Test
    void should_parse_js_file() throws URISyntaxException, IOException {
        // Given
        JsProject jsProject = new JsProjectImpl("tus.prc.actionCombine", fileService, mainFactory);

        // When
        var elementsFromProject = jsProject.getElements();
        var blocksFromProject = convertElementsToBlocks(elementsFromProject, mainFactory);

        // Then
        assertThat(blocksFromProject.size()).isEqualTo(20);
    }

    @Test
    void should_parse_minimal_two_function_js_content() throws URISyntaxException, IOException {
        // Given
        JsProject jsProject = new JsProjectImpl("CreateCustomer_Dev_minimal", fileService, mainFactory);

        // When - Parse the JS content through JsProject interface
        var elementsFromProject = jsProject.getElements();
        var blocksFromProject = convertElementsToBlocks(elementsFromProject, mainFactory);

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
    
    private java.util.List<Block> convertElementsToBlocks(java.util.List<JsNode> elements, MainFactory mainFactory) {
        java.util.List<Block> allBlocks = new java.util.ArrayList<>();
        
        for (JsNode element : elements) {
            try {
                BlockFromJsNode parser = mainFactory.getBlockBuilder(element.getElementName());
                BlockFromJsElementResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                throw new IllegalArgumentException("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }

}
