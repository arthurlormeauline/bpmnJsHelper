package com.protectline.tobpmn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.protectline.util.FileUtil.compareBpmnFiles;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsProjectToBpmnTest {

    @TempDir
    Path tempDir;

    private Path testWorkingDirectory;
    private Path jsProjectOutputPath;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = Path.of(Objects.requireNonNull(
                getClass().getClassLoader().getResource("tobpmn")).toURI());
        
        testWorkingDirectory = tempDir.resolve("testData");
        Files.createDirectories(testWorkingDirectory);
        
        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
        
        jsProjectOutputPath = testWorkingDirectory.resolve("output");
    }
    
    private void copyDirectory(Path source, Path target) throws IOException {
        Files.walk(source)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = target.resolve(source.relativize(sourcePath));
                        if (Files.isDirectory(sourcePath)) {
                            Files.createDirectories(targetPath);
                        } else {
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    void should_update_bpmn_from_jsProject() throws IOException {
        // Given
        String processName = "simplify";
        
        File jsProjectDir = jsProjectOutputPath.resolve(processName).toFile();
        
        if (!jsProjectDir.exists() || !jsProjectDir.isDirectory()) {
            throw new RuntimeException("JS project directory not found: " + processName);
        }

        // When
        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(testWorkingDirectory);
        jsProjectToBpmn.updateBpmn(processName);

        // Then
        Path bpmnFile = testWorkingDirectory.resolve("input/simplify.bpmn");
        compareBpmnFiles(bpmnFile, testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn"));
    }

}
