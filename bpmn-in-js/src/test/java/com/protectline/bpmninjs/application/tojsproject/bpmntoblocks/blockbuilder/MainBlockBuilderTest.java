package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.blockbuilder;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.BlockBuilder;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.MainBlockBuilder;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;
import com.protectline.bpmninjs.bpmndocument.model.NodeType;
import com.protectline.bpmninjs.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.protectline.bpmninjs.application.tojsproject.stub.StubBlock.getExpectedBlock;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MainBlockBuilderTest {

    private MainBlockBuilder builder;
    private FileUtil fileUtil;


    @BeforeEach
    void setup() throws URISyntaxException {
        builder = new MainBlockBuilder();
        var testDirectory = "tojsproject";
        var resourcePath = getResourcePath(MainBlockBuilderTest.class, testDirectory);
        fileUtil = new FileUtil(resourcePath);
    }

    @ParameterizedTest
    @MethodSource("shouldExtractAllBlocksData")
    void should_extract_all_blocks(List<BlockBuilder> builders) throws IOException {
        // Given
        var process = "simplify";
        var document = new BpmnCamundaDocument(fileUtil.getBpmnFile(process).toFile());

        // When
        List<Block> actual = builder
                .registerSubBlockBuilders(builders)
                .getBlocks(document);

        // Then
        var expectedDelayDefinition = getExpectedBlock("Activity_0q6wfo8", "Delay_definition_0", "delay definition script", NodeType.SCRIPT);
        var expectedGetDeviceByName_0 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_0", "get device by name url script", NodeType.SERVICE_TASK);
        var expectedGetDeviceByName_1 = getExpectedBlock("Activity_0rwauzg", "Get_device_by_name_1", "get device by name output script", NodeType.SERVICE_TASK);
        var expectedStartEvent = getExpectedBlock("Event_1u1d1qi", "Event_1u1d1qi_0", "start event script", NodeType.START);

        assertThat(actual).isEqualTo(List.of(expectedDelayDefinition, expectedGetDeviceByName_0, expectedGetDeviceByName_1, expectedStartEvent));
    }

    private static Stream<Arguments> shouldExtractAllBlocksData() {
        var scripBuilder = new ArrayList<BlockBuilder>();
        scripBuilder.add(new FunctionBlockBuilder());
        return Stream.of(
                Arguments.of(scripBuilder)
        );
    }
}
