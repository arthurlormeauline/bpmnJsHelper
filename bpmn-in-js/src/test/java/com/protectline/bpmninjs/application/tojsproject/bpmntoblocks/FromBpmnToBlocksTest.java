package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;
import static com.protectline.bpmninjs.util.AssertUtil.assertBlocksEqualIgnoringId;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;

class FromBpmnToBlocksTest {

    private FromBpmnToBlocks fromBpmnToBlocks;
    private FileUtil fileUtil;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testDirectory = "tojsproject";
        var resourcePath = getResourcePath(FromBpmnToBlocksTest.class, testDirectory);
        fileUtil = new FileUtil(resourcePath);
        MainFactory mainFactory = MainFactoryTestUtil.createWithDefaults(fileUtil);
        BlockWriter blockWriter = new BlockWriter();
        fromBpmnToBlocks = new FromBpmnToBlocks(fileUtil, mainFactory.getBlockBuilders(), blockWriter);
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
        assertBlocksEqualIgnoringId(actualBlocks, expectedBlock);
    }
}
