package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.application.tobpmn.blockstobpmn.FromBlockToBpmn;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;
import com.protectline.files.FileUtil;
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
import static com.protectline.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FromBpmnToBlocksTest {

    private FromBpmnToBlocks fromBpmnToBlocks;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testFolderName = "toJsProject";
        var resourcePath = getResourcePath(FromBpmnToBlocksTest.class, testFolderName);
        fileUtil = new FileUtil(resourcePath.resolve(testFolderName));
        fromBpmnToBlocks = new FromBpmnToBlocks(fileUtil.getWorkingDirectory());
    }


    @Test
    void should_extract_all_blocks() throws IOException {
        // Given
        var process = "simplify";
        var workingdir = fileUtil.getWorkingDirectory();
        // When
        fromBpmnToBlocks.createBlocksFromBpmn(process);

        // Then
        var expectedBlock = readBlocksFromFile(workingdir.resolve("expectedBlocks").resolve(process).resolve(process+".json"));
        var actualBlocks = readBlocksFromFile(fileUtil.getBlocksFile(process));
        assertThat(actualBlocks).isEqualTo(expectedBlock);
    }
}
