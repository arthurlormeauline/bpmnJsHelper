package com.protectline.bpmninjs.engine.tobpmn.blockstobpmn;

import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.protectline.bpmninjs.util.FileUtil.*;

class FromBlockToBpmnTest {
    @TempDir
    private Path tempDir;
    private FromBlockToBpmn fromBlockToBpmn;
    private FileService fileService;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        // Copier toute la structure de test vers le répertoire temporaire
        String testDirectory = "tobpmn";
        Path resourcesPath = getResourcePath(FromBlockToBpmnTest.class, testDirectory);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileService = new FileService(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);

        MainFactory mainFactory = MainFactoryTestUtil.createWithDefaults(fileService);
        fromBlockToBpmn = new FromBlockToBpmn(fileService, mainFactory);
    }

    @Test
    void should_update_bpmn_document() throws IOException {
        // Given
        String processName = "simplifyInter";
        var testWorkingDirectory = fileService.getWorkingDirectory();
        Path bpmnFile = testWorkingDirectory.resolve("input/simplifyInter.bpmn");

        // When
        fromBlockToBpmn.updateBpmnFromBlocks(processName);

        // Then
        Path expectedModify = testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn");
        compareBpmnFiles(bpmnFile, expectedModify);
    }
}
