package com.protectline.application.tobpmn.blockstobpmn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.protectline.util.FileUtil.compareBpmnFiles;
import static com.protectline.util.FileUtil.copyDirectory;

class FromBlockToBpmnTest {
    @TempDir
    private Path tempDir;
    private Path testWorkingDirectory;

    private FromBlockToBpmn fromBlockToBpmn;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = Path.of(Objects.requireNonNull(
                FromBlockToBpmnTest.class.getClassLoader().getResource("tobpmn")).toURI());

        testWorkingDirectory = tempDir.resolve("testData");
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);

        fromBlockToBpmn = new FromBlockToBpmn(testWorkingDirectory);
    }

    @Test
    void should_update_bpmn_document() throws IOException {
        // Given
        // todo
        String processName = "simplifyInter";
        Path bpmnFile = testWorkingDirectory.resolve("input/simplifyInter.bpmn");

        // When
        fromBlockToBpmn.updateBpmnFromBlocks(processName);

        // Then
        Path expectedModify = testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn");
        compareBpmnFiles(bpmnFile, expectedModify);
    }
}
