package com.protectline.application.tobpmn.blockstobpmn.bpmnupdate;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.application.tobpmn.blockstobpmn.bpmnupdater.BpmnDocumentUpdater;
import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.io.TempDir;

import static com.protectline.application.tojsproject.stub.StubBlock.getExpectedBlock;
import static com.protectline.util.FileUtil.*;


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

        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "NEW : delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "NEW : get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "NEW : get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "NEW : start event script", NodeType.START);

        mainUpdater = new BpmnDocumentUpdater(List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent));
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
