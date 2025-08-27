package com.protectline.application.tobpmn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.protectline.util.FileUtil.compareBpmnFiles;
import static com.protectline.util.FileUtil.copyDirectory;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsProjectToBpmnTest {

    @TempDir
    private Path tempDir;

    private Path testWorkingDirectory;
    private Path jsProjectOutputPath;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = Path.of(Objects.requireNonNull(
                JsProjectToBpmnTest.class.getClassLoader().getResource("tobpmn")).toURI());

        testWorkingDirectory = tempDir.resolve("testData");
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
        
        jsProjectOutputPath = testWorkingDirectory.resolve("output");
    }
    

    @Test
    void should_update_bpmn_from_jsProject() throws IOException {
        // Given
        String processName = "simplify";
        Path bpmnFile = testWorkingDirectory.resolve("input/simplify.bpmn");

        File jsProjectDir = jsProjectOutputPath.resolve(processName).toFile();

        if (!jsProjectDir.exists() || !jsProjectDir.isDirectory()) {
            throw new RuntimeException("JS project directory not found: " + processName);
        }

        // When
        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(testWorkingDirectory);
        jsProjectToBpmn.updateBpmn(processName);

        // Then
        compareBpmnFiles(bpmnFile, testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn"));
    }

}
