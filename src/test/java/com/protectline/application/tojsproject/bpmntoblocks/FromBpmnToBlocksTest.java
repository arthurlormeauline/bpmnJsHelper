package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.protectline.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;
import static com.protectline.util.FileUtil.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FromBpmnToBlocksTest {

    private FromBpmnToBlocks fromBpmnToBlocks;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testDirectory = "tojsproject";
        var resourcePath = getResourcePath(FromBpmnToBlocksTest.class, testDirectory);
        fileUtil = new FileUtil(resourcePath);
        fromBpmnToBlocks = new FromBpmnToBlocks(fileUtil);
    }


    @Test
    void should_extract_all_blocks() throws IOException {
        // Given
        var process = "simplify";
        var workingdir = fileUtil.getWorkingDirectory();

        // When
        fromBpmnToBlocks.createBlocksFromBpmn(process);

        // Then
        var expectedBlock = readBlocksFromFile(workingdir.resolve("expectedBlocks").resolve(process).resolve(process + ".json"));
        var actualBlocks = readBlocksFromFile(fileUtil.getBlocksFile(process));
        assertThat(actualBlocks).isEqualTo(expectedBlock);
    }
}
