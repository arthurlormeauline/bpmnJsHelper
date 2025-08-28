package com.protectline.application.tobpmn.blockstobpmn;

import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.protectline.util.FileUtil.*;

class FromBlockToBpmnTest {
    @TempDir
    private Path tempDir;
    private FromBlockToBpmn fromBlockToBpmn;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        // Copier toute la structure de test vers le répertoire temporaire
        String testDirectory = "tobpmn";
        Path resourcesPath = getResourcePath(FromBlockToBpmnTest.class, testDirectory);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileUtil = new FileUtil(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);

        fromBlockToBpmn = new FromBlockToBpmn(fileUtil);
    }

    @Test
    void should_update_bpmn_document() throws IOException {
        // Given
        String processName = "simplifyInter";
        var testWorkingDirectory = fileUtil.getWorkingDirectory();
        Path bpmnFile = testWorkingDirectory.resolve("input/simplifyInter.bpmn");

        // When
        fromBlockToBpmn.updateBpmnFromBlocks(processName);

        // Then
        Path expectedModify = testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn");
        compareBpmnFiles(bpmnFile, expectedModify);
    }
}
