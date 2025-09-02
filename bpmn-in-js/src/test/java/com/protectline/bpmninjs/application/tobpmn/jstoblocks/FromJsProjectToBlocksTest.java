package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.application.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static com.protectline.bpmninjs.util.AssertUtil.assertBlocksAreEqualToExpected;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;

class FromJsProjectToBlocksTest {

    public static final String TEST_DIRECTORY = "tobpmn";

    @TempDir
    Path tempDir;

    private FileUtil fileUtil;
    private FromJsProjectToBlocks fromJsProjectToBlocks;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = getResourcePath(FromJsProjectToBlocksTest.class, TEST_DIRECTORY);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileUtil = new FileUtil(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
        fromJsProjectToBlocks = new FromJsProjectToBlocks(fileUtil, MainFactoryTestUtil.createWithDefaults(fileUtil));

    }

    private void copyDirectory(Path source, Path target) throws IOException {
        try (var walk = Files.walk(source)) {
            walk.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy " + sourcePath + " to " + target, e);
                }
            });
        }
    }

    @Test
    void should_create_blocks_from_js_project() throws IOException {
        // Given
        var process = "simplify";

        // When
        fromJsProjectToBlocks.updateBlockFromJsProject(process);

        // Then
        assertBlocksAreEqualToExpected(fileUtil, process);
    }

    @Test
    void should_create_blocks_from_js_project_with_backslash() throws IOException {
        // Given
        var process = "simplify-with-backslash";

        // When
        fromJsProjectToBlocks.updateBlockFromJsProject(process);

        // Then
        assertBlocksAreEqualToExpected(fileUtil, process);
    }

    @Test
    void should_create_blocks_from_js_project_with_problematic_functions() throws IOException {
        // Given
        var process = "test-parsing-issue";

        // When
        fromJsProjectToBlocks.updateBlockFromJsProject(process);

        // Then
        assertBlocksAreEqualToExpected(fileUtil, process);
    }

    @Test
    void should_create_blocks_from_js_project_CreateCustomer_Dev() throws IOException {
        // Given - Real CreateCustomer_Dev BpmnRunner.js
        var process = "CreateCustomer_Dev";

        // When - Parse the JS project to blocks
        fromJsProjectToBlocks.updateBlockFromJsProject(process);

        // Then - Should successfully parse all 26 functions
        assertBlocksAreEqualToExpected(fileUtil, process);
    }
}
