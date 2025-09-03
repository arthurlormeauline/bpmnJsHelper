package com.protectline.bpmninjs.application.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.files.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.protectline.bpmninjs.util.AssertUtil.assertJsProjectIsEqualToExpected;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;

class FromBlockToJsProjectTest {

    private FromBlockToJsProject fromBlockToJsProject;
    private com.protectline.bpmninjs.files.FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        Path workingDirectory = getResourcePath(FromBlockToJsProjectTest.class, "tojsproject");
        fileUtil = new FileUtil(workingDirectory);
        MainFactory mainFactory = MainFactoryTestUtil.createWithDefaults(fileUtil);
        fromBlockToJsProject = new FromBlockToJsProject(fileUtil, new BlockWriter(), mainFactory);
        
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
