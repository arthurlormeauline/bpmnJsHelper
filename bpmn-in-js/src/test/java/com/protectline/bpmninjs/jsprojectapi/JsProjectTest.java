package com.protectline.bpmninjs.jsprojectapi;

import com.protectline.bpmninjs.model.jsproject.api.JsNode;
import com.protectline.bpmninjs.model.jsproject.api.JsProject;
import com.protectline.bpmninjs.model.jsproject.JsProjectImpl;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.engine.tobpmn.jstoblocks.BlockFromElementResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.protectline.bpmninjs.engine.tojsproject.stub.StubBlock.equalsIgnoringId;
import static com.protectline.bpmninjs.engine.tojsproject.stub.StubBlock.getExpectedBlocksWithUUID;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectTest {

    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testDirectory = "tojsproject";
        fileUtil = new FileUtil(getResourcePath(JsProjectTest.class, testDirectory));
    }

    @Test
    void should_get_elements_from_project() throws IOException {
        // Given
        var process = "simplify";
        JsProject jsProject = new JsProjectImpl(process, fileUtil, MainFactoryTestUtil.createWithDefaults(fileUtil));
        var mainFactory = MainFactoryTestUtil.createWithDefaults(fileUtil);

        // When
        var elements = jsProject.getElements();
        
        // Conversion des Element en Block pour la comparaison
        var actualBlocks = convertElementsToBlocks(elements, mainFactory);

        // Then
        List<Block> expectedBlocksWithUUID = getExpectedBlocksWithUUID();
        equalsIgnoringId(actualBlocks, expectedBlocksWithUUID);
    }
    
    private List<Block> convertElementsToBlocks(List<JsNode> elements, com.protectline.bpmninjs.engine.mainfactory.MainFactory mainFactory) throws IOException {
        List<Block> allBlocks = new java.util.ArrayList<>();
        
        for (JsNode element : elements) {
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
