package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.util.AssertUtil;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockFromElement;
import com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockFromElementResult;
import com.protectline.bpmninjs.xmlparser.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.protectline.bpmninjs.application.tojsproject.stub.StubBlock.equalsIgnoringId;
import static com.protectline.bpmninjs.application.tojsproject.stub.StubBlock.getExpectedBlocksWithUUID;
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
    
    private List<Block> convertElementsToBlocks(List<Element> elements, com.protectline.bpmninjs.application.mainfactory.MainFactory mainFactory) throws IOException {
        List<Block> allBlocks = new java.util.ArrayList<>();
        
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
