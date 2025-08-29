package com.protectline.jsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;
import com.protectline.util.AssertUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.protectline.application.tojsproject.stub.StubBlock.getExpectedBlocksWithUUID;
import static com.protectline.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JsProjectTest {

    private JsProject jsProject;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException {
        var testDirectory = "tojsproject";
        fileUtil = new FileUtil(getResourcePath(JsProjectTest.class, testDirectory));
        jsProject = new JsProject(fileUtil);
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
        assertThat(actualBlocks.containsAll(expectedBlocksWithUUID)).isTrue();
    }
}
