package com.protectline.application.tobpmn;

import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.protectline.util.FileUtil.*;

class JsProjectToBpmnTest {

    @TempDir
    private Path tempDir;
    private FileUtil fileUtil;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        String testDirectory = "tobpmn";
        Path resourcesPath = getResourcePath(JsProjectToBpmnTest.class, testDirectory);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileUtil = new FileUtil(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
    }



    @Test
    void should_update_bpmn_from_jsProject() throws IOException {
        // Given
        String processName = "simplify";
        var testWorkingDirectory = fileUtil.getWorkingDirectory();
        Path bpmnFile = testWorkingDirectory.resolve("input/simplify.bpmn");

        File jsProjectDir = fileUtil.getJsProjectDirectory(processName).toFile();

        if (!jsProjectDir.exists() || !jsProjectDir.isDirectory()) {
            throw new RuntimeException("JS project directory not found: " + processName);
        }

        // When
        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(fileUtil);
        jsProjectToBpmn.updateBpmn(processName);

        // Then
        compareBpmnFiles(bpmnFile, testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn"));
    }

}
