package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.util.AssertUtil;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static com.protectline.bpmninjs.application.tojsproject.stub.StubBlock.getExpectedBlocksWithUUID;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;

class JsProjectServiceTest {

    public static final String TEST_DIRECTORY = "tojsproject";
    @TempDir
    Path tempDir;
    private JsProjectService jsProjectService;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = getResourcePath(JsProjectServiceTest.class, TEST_DIRECTORY);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileUtil = new FileUtil(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
        
        jsProjectService = new JsProjectService(fileUtil, MainFactoryTestUtil.createWithDefaults(fileUtil));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Force cleanup of temp directory
        if (Files.exists(tempDir)) {
            try (var walk = Files.walk(tempDir)) {
                walk.sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // Ignore cleanup errors
                        }
                    });
            }
        }
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
    void should_update_project_from_blocks() throws IOException {
        // Given
        var process = "simplify";
        var blocks = getExpectedBlocksWithUUID();
        fileUtil.deleteJsDirectoryIfExists(process);
        fileUtil.copyTemplateToJsDirectory(process);
        
        // Création de l'instance JsProject pour ce process
        JsProject jsProject = new JsProjectImpl(process, fileUtil, MainFactoryTestUtil.createWithDefaults(fileUtil));

        // When
        jsProjectService.updateProject(jsProject, blocks);

        // Then
        AssertUtil.assertJsProjectIsEqualToExpected(fileUtil, process);
    }
}