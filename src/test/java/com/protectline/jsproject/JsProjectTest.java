package com.protectline.jsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;
import com.protectline.util.AssertUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.protectline.application.tojsproject.stub.StubBlock.getExpectedNewBlocks;
import static com.protectline.util.FileUtil.getResourcePath;

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
    void updateProject() throws IOException {
        // Given
        var process = "simplify";
        var blocks = getExpectedNewBlocks();

        // When
        jsProject.updateProject(process, blocks);

        // Then
        AssertUtil.assertJsProjectIsCreated(fileUtil, process);
    }


    @Test
    void updateBlocks() {
        // todo
    }
}
