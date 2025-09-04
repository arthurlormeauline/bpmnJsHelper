package com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks.blockbuilder;

import com.protectline.bpmninjs.model.bpmndocument.api.model.NodeType;
import com.protectline.bpmninjs.model.bpmndocument.camundaimpl.BpmnCamundaDocument;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.application.function.tojsproject.blockfrombpmnnode.FunctionBlockFromBpmnNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.protectline.bpmninjs.engine.tojsproject.stub.StubBlock.getExpectedBlock;
import static com.protectline.bpmninjs.engine.tojsproject.stub.StubBlock.equalsIgnoringId;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class FunctionBlockFromBpmnNodeTest {

   private FunctionBlockFromBpmnNode builder;
   private FileService fileService;

    @BeforeEach
    void setup() throws URISyntaxException {
        builder = new FunctionBlockFromBpmnNode();
        var testFolderName = "tojsproject";
        var resourcePath = getResourcePath(FunctionBlockFromBpmnNodeTest.class, testFolderName);
        fileService = new FileService(resourcePath);
    }

    @Test
    void should_get_function_block_from_document() throws IOException {
        // Given
        var process = "simplify";
        BpmnCamundaDocument document = new BpmnCamundaDocument(fileService.getBpmnFile(process).toFile());

        // When
        var actual = builder.getBlocks(document);

        // Then
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "start event script", NodeType.START);

        List<Block> expected = List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent);
        
        assertThat(equalsIgnoringId(actual, expected)).isTrue();
    }

    @Test
    void should_get_function_block_from_document_with_backslash() throws IOException {
        // Given
        var process = "simplify-with-backslash";
        BpmnCamundaDocument document = new BpmnCamundaDocument(fileService.getBpmnFile(process).toFile());

        // When
        var actual = builder.getBlocks(document);

        // Then
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "start event / script", NodeType.START);

        assertThat(equalsIgnoringId(actual, List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent))).isTrue();
    }

}
