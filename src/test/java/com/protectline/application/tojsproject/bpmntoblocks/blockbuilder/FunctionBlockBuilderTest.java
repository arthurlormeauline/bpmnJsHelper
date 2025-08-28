package com.protectline.application.tojsproject.bpmntoblocks.blockbuilder;

import com.protectline.bpmndocument.model.NodeType;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;
import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.protectline.application.tojsproject.stub.StubBlock.getExpectedBlock;
import static com.protectline.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.setAllowComparingPrivateFields;


class FunctionBlockBuilderTest {

   private FunctionBlockBuilder builder;
   private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException {
        builder = new FunctionBlockBuilder();
        var testFolderName = "toJsProject";
        var resourcePath = getResourcePath(FunctionBlockBuilderTest.class, testFolderName);
        var testWorkingDirectory = resourcePath.resolve(testFolderName);
        fileUtil= new FileUtil(testWorkingDirectory);
    }

    @Test
    void should_get_function_block_from_document() throws IOException {
        // Given
        var testWorkingDirectory = fileUtil.getWorkingDirectory();
        BpmnCamundaDocument document = new BpmnCamundaDocument(testWorkingDirectory, "simplify");

        // When
        var actual = builder.getBlocks(document);

        // Then
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "start event script", NodeType.START);

        assertThat(actual).isEqualTo(List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent));
    }
}
