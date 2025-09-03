package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.util.AssertUtil;
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

    private JsProject jsProject;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testDirectory = "tojsproject";
        fileUtil = new FileUtil(getResourcePath(JsProjectTest.class, testDirectory));
        jsProject = new JsProject(fileUtil, MainFactoryTestUtil.createWithDefaults(fileUtil));
    }

    @Test
    void should_update_project() throws IOException {
        // Given
        var process = "simplify";
        var blocks = getExpectedBlocksWithUUID();

        // When
        jsProject.updateProject(process, blocks);

        // Then
        AssertUtil.assertJsProjectIsEqualToExpected(fileUtil, process);
    }


    @Test
    void should_get_blocks_from_project() throws IOException {
        // Given
        var process = "simplify";

        // When
        var actualBlocks = jsProject.getBlocks(process);

        // Then
        List<Block> expectedBlocksWithUUID = getExpectedBlocksWithUUID();
        equalsIgnoringId(actualBlocks, expectedBlocksWithUUID);
    }
}
