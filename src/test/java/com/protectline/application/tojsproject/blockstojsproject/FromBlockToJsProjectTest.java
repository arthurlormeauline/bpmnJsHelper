package com.protectline.application.tojsproject.blockstojsproject;

import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.protectline.util.AssertUtil.assertJsProjectIsEqualToExpected;
import static com.protectline.util.FileUtil.getResourcePath;

// WONT PASS
class FromBlockToJsProjectTest {

    private FromBlockToJsProject fromBlockToJsProject;
    private com.protectline.files.FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException {
        fileUtil = new FileUtil(getResourcePath(FromBlockToJsProjectTest.class, "tojsproject"));
        fromBlockToJsProject = new FromBlockToJsProject(fileUtil);
    }

    @Test
    void should_create_js_project_from_blocks() throws IOException {
        // Given
        var process= "simplify";

        // When
        fromBlockToJsProject.updateJsProjectFromBlocks(process);

        // Then
        assertJsProjectIsEqualToExpected(fileUtil, process);
    }

}
