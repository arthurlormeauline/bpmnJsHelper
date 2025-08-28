package com.protectline.application.tojsproject.blockstojsproject;

import com.protectline.files.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.protectline.util.AssertUtil.assertJsProjectIsEqualToExpected;
import static com.protectline.util.FileUtil.getResourcePath;

class FromBlockToJsProjectTest {

    private FromBlockToJsProject fromBlockToJsProject;
    private com.protectline.files.FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        Path workingDirectory = getResourcePath(FromBlockToJsProjectTest.class, "tojsproject");
        fileUtil = new FileUtil(workingDirectory);
        fromBlockToJsProject = new FromBlockToJsProject(fileUtil);
        
        Path sourceDir = workingDirectory.resolve("expectedblocks").resolve("simplify");
        Path targetDir = workingDirectory.resolve("blocks").resolve("simplify");
        if (Files.exists(sourceDir)) {
            FileUtils.copyDirectory(sourceDir.toFile(), targetDir.toFile());
        }
    }

    @AfterEach
    void tearDown() throws URISyntaxException, IOException {
        // Clean up blocks directory after each test
        Path workingDirectory = getResourcePath(FromBlockToJsProjectTest.class, "tojsproject");
        Path blocksDir = workingDirectory.resolve("blocks");
        if (Files.exists(blocksDir)) {
            FileUtils.deleteDirectory(blocksDir.toFile());
        }
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
