package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.application.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.protectline.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FromBpmnToBlocksTest {

    private FromBpmnToBlocks fromBpmnToBlocks;
    private String testFolderName;
    private Path resourcePath;
    private Path workingdir;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        testFolderName = "toJsProject";
        resourcePath = Path.of(Objects.requireNonNull(
                FromBpmnToBlocksTest.class.getClassLoader().getResource(testFolderName)).toURI()).getParent();
        workingdir = resourcePath.resolve(testFolderName);
        fromBpmnToBlocks = new FromBpmnToBlocks(workingdir);
    }


    @Test
    void should_extract_all_blocks() throws IOException {
        // Given
        var process = "simplify";

        // When
        fromBpmnToBlocks.createBlocksFromBpmn(process);

        // Then
//        var expectedBlock = readBlocksFromFile(workingdir.resolve("expectedBlocks/"+process));
        var actualBlocks = readBlocksFromFile(workingdir.resolve("blocks/"+process));
       // assertThat(actualBlocks).isEqualTo(expectedBlock);
    }
}
