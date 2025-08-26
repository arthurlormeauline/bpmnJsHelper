package com.protectline.tobpmn.bpmnupdate;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.tobpmn.bpmnupdate.bpmndocumentupdater.MainBpmnDocumentUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import static com.protectline.tojsproject.util.StubBlock.getExpectedBlock;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MainBpmnDocumentUpdaterTest {


    private MainBpmnDocumentUpdater mainUpdater;
    private String testFolderName;
    private Path resourcePath;
    private Path testWorkingDirectory;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        testFolderName = "tests";

        resourcePath = Path.of(Objects.requireNonNull(
                MainBpmnDocumentUpdaterTest.class.getClassLoader().getResource(testFolderName)).toURI()).getParent();

        testWorkingDirectory = resourcePath.resolve(testFolderName);

        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "NEW : delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "NEW : get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "NEW : get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "NEW : start event script", NodeType.START);

        mainUpdater = new MainBpmnDocumentUpdater(List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent));
    }

    @Test
    void should_update_bpmn_document() throws IOException {
        // Given
        BpmnCamundaDocument document = new BpmnCamundaDocument(testWorkingDirectory, "simplify");
        Path bpmnFile = testWorkingDirectory.resolve("input/simplify.bpmn");

        // When
        mainUpdater.updateDocument(document);

        // Then
        Path expectedModify = testWorkingDirectory.resolve("input/simplify_expected_modify.bpmn");
        byte[] originalContent = Files.readAllBytes(bpmnFile);
        byte[] recreatedContent = Files.readAllBytes(expectedModify);

        // Restore initial state before assertion
        Path backupFile = testWorkingDirectory.resolve("input/simplify_back_up.bpmn");
        Files.copy(backupFile, bpmnFile, StandardCopyOption.REPLACE_EXISTING);

        assertTrue(java.util.Arrays.equals(originalContent, recreatedContent));
    }

}
