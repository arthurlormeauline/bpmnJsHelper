package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.application.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockFromElementResult;
import com.protectline.bpmninjs.xmlparser.Element;
import com.protectline.bpmninjs.common.block.Block;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectBlocksBuilderTest {

    MainFactory mainFactory;
    FileUtil fileUtil;

    @BeforeEach
    void setup() throws IOException, URISyntaxException {
        fileUtil = new FileUtil(com.protectline.bpmninjs.util.FileUtil.getResourcePath(JsProjectBlocksBuilderTest.class, "tobpmn"));
        mainFactory = MainFactoryTestUtil.createWithDefaults(fileUtil);
    }


    @Test
    void should_parse_js_file() throws URISyntaxException, IOException {
        // Given
        JsProject jsProject = new JsProjectImpl("tus.prc.actionCombine", fileUtil, mainFactory);

        // When
        var elementsFromProject = jsProject.getElements();
        var blocksFromProject = convertElementsToBlocks(elementsFromProject, mainFactory);

        // Then
        assertThat(blocksFromProject.size()).isEqualTo(20);
    }

    @Test
    void should_parse_minimal_two_function_js_content() throws URISyntaxException, IOException {
        // Given
        JsProject jsProject = new JsProjectImpl("CreateCustomer_Dev_minimal", fileUtil, mainFactory);

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
    
    private java.util.List<Block> convertElementsToBlocks(java.util.List<Element> elements, MainFactory mainFactory) {
        java.util.List<Block> allBlocks = new java.util.ArrayList<>();
        
        for (Element element : elements) {
            try {
                BlockFromElement parser = mainFactory.getBlockBuilder(element.getElementName());
                BlockFromElementResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                throw new IllegalArgumentException("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }

}
