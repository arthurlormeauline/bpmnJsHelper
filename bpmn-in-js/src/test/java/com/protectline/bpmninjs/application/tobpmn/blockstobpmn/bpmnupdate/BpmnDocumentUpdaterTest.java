package com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdate;

import com.protectline.bpmninjs.application.tobpmn.blockstobpmn.bpmnupdater.BpmnDocumentUpdater;
import com.protectline.bpmninjs.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.protectline.bpmninjs.application.tojsproject.stub.StubBlock.getExpectedNewBlocks;
import static com.protectline.bpmninjs.util.FileUtil.*;


class BpmnDocumentUpdaterTest {

    @TempDir
    private Path tempDir;
    private BpmnDocumentUpdater mainUpdater;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
         // Copier toute la structure de test vers le répertoire temporaire
        var testDirectory = "tobpmn";
        Path resourcesPath = getResourcePath(BpmnDocumentUpdaterTest.class, testDirectory);

        var testWorkingDirectory = tempDir.resolve("testData");
        fileUtil = new FileUtil(testWorkingDirectory);
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);

        List<Block> blocks = getExpectedNewBlocks();
        mainUpdater = new BpmnDocumentUpdater(blocks);
    }


    @Test
    void should_update_bpmn_document() throws IOException {
        // Given
        String processName = "simplify";
        var testWorkingDirectory = fileUtil.getWorkingDirectory();
        BpmnCamundaDocument document = new BpmnCamundaDocument(fileUtil.getBpmnFile(processName).toFile());
        Path bpmnFile = fileUtil.getBpmnFile(processName);

        // When
        mainUpdater.updateDocument(document);

        // Then
        document.writeToFile(bpmnFile.toFile());

        Path expectedModify = testWorkingDirectory.resolve("expectedBpmnFile").resolve("simplify_expected_modify.bpmn");
        compareBpmnFiles(bpmnFile, expectedModify);
    }
}
