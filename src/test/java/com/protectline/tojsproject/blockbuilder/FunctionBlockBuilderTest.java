package com.protectline.tojsproject.blockbuilder;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.bpmndocument.model.BpmnPath;
import com.protectline.jsproject.model.block.FunctionBlock;
import com.protectline.tojsproject.block.functionblock.FunctionBlockBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class FunctionBlockBuilderTest {

    FunctionBlockBuilder builder;
    String testFolderName;

    Path resourcePath;

    Path testWorkingDirectory;

    @BeforeEach
    void setup() throws URISyntaxException {
        builder = new FunctionBlockBuilder();
        testFolderName = "tests";

        resourcePath = Path.of(Objects.requireNonNull(
                FunctionBlockBuilderTest.class.getClassLoader().getResource(testFolderName)).toURI()).getParent();
        testWorkingDirectory = resourcePath.resolve(testFolderName);
    }

    @Test
    void should_get_function_block_from_document() {
        // Given
        BpmnCamundaDocument bpmnCamundaDocument = new BpmnCamundaDocument(testWorkingDirectory, "simplify");

        // When
        var actual = builder.getBlocks(bpmnCamundaDocument);

        // Then
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay definition_0", "delay definition script");
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get device by name_0", "get device by name url script");
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get device by name_1", "get device by name output script");
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "start event script");

        assertThat(actual).isEqualTo(List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent));
    }

    private static FunctionBlock getExpectedBlock(String id, String name, String script) {
        BpmnPath expectedPath = new BpmnPath(id);
        String expectedName = name;
        String expectedContent = script;
        return new FunctionBlock(expectedPath, expectedName, expectedContent);
    }

}
