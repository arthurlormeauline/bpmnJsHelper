package com.protectline.jsproject;

import com.protectline.files.FileUtil;
import com.protectline.util.AssertUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.protectline.application.tojsproject.stub.StubBlock.getExpectedNewBlocks;
import static com.protectline.util.FileUtil.getResourcePath;

// WONT PASS
class JsProjectUpdaterTest {

    private JsProjectUpdater jsProjectUpdater;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException {
        var testDirectory = "tojsproject";
        fileUtil = new FileUtil(getResourcePath(JsProjectUpdaterTest.class, testDirectory));
        jsProjectUpdater = new JsProjectUpdater(fileUtil);
    }

    @Test
    void should_update_project_from_blocks() throws IOException {
        // Given
        var process = "simplify";
        var blocks = getExpectedNewBlocks();

        // When
        jsProjectUpdater.updateProject(process, blocks);

        // Then
        AssertUtil.assertJsProjectIsEqualToExpected(fileUtil, process);
    }

}
