package com.protectline.application.tojsproject;

import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.protectline.util.DirectoryComparisonUtil.areDirectoriesEqual;
import static com.protectline.util.FileUtil.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertTrue;

// WONT PASS
class BpmnToJsProjectTest {

    @TempDir
    Path tempDir;
    private FileUtil fileUtil;


    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        String testDirectory = "toJsProject";
        Path resourcesPath = getResourcePath(BpmnToJsProjectTest.class, testDirectory);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileUtil = new FileUtil(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
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
    void should_create_jsProject() throws IOException {
        // Given
        var process = "simplify";

        // When
        BpmnToJS bpmnToJs = new BpmnToJS(fileUtil);
        bpmnToJs.createProject(process);

        // Then
        Path outputDir = fileUtil.getJsProjectDirectory(process);
        assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
        assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

        Path expectedProject = fileUtil.getWorkingDirectory().resolve("expectedJsProject").resolve(process);
        areDirectoriesEqual(outputDir, expectedProject);
    }
}
