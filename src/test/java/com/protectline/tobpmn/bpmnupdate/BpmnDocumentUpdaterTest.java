package com.protectline.tobpmn.bpmnupdate;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.translate.fromblocktobpmn.bpmndocumentupdater.BpmnDocumentUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.io.TempDir;

import static com.protectline.util.FileUtil.compareBpmnFiles;
import static com.protectline.util.FileUtil.copyDirectory;
import static com.protectline.tojsproject.stub.StubBlock.getExpectedBlock;


class BpmnDocumentUpdaterTest {

    @TempDir
    private Path tempDir;
    private Path testWorkingDirectory;

    private BpmnDocumentUpdater mainUpdater;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
         // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = Path.of(Objects.requireNonNull(
                BpmnDocumentUpdaterTest.class.getClassLoader().getResource("tobpmn")).toURI());

        testWorkingDirectory = tempDir.resolve("testData");
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
        BpmnCamundaDocument document = new BpmnCamundaDocument(testWorkingDirectory, processName);
        Path bpmnFile = testWorkingDirectory.resolve("input/simplify.bpmn");

        // When
        mainUpdater.updateDocument(document);

        // Then
        document.writeToFile(bpmnFile.toFile());

        Path expectedModify = testWorkingDirectory.resolve("expectedBpmnFile/simplify_expected_modify.bpmn");
        compareBpmnFiles(bpmnFile, expectedModify);
    }
}
