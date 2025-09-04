package com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.model.block.persist.BlockUtil;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.protectline.bpmninjs.model.block.persist.BlockUtil.readBlocksFromFile;
import static com.protectline.bpmninjs.util.AssertUtil.assertBlocksEqualIgnoringId;
import static com.protectline.bpmninjs.util.FileUtil.getResourcePath;

class FromBpmnToBlocksTest {

    private FromBpmnToBlocks fromBpmnToBlocks;
    private FileService fileService;

    @BeforeEach
    void setup() throws URISyntaxException, IOException {
        var testDirectory = "tojsproject";
        var resourcePath = getResourcePath(FromBpmnToBlocksTest.class, testDirectory);
        fileService = new FileService(resourcePath);
        MainFactory mainFactory = MainFactoryTestUtil.createWithDefaults(fileService);
        BlockUtil blockUtil = new BlockUtil();
        fromBpmnToBlocks = new FromBpmnToBlocks(fileService, mainFactory.getBlockBuilders(), blockUtil);
    }


    @Test
    void should_extract_all_blocks() throws IOException {
        // Given
        var process = "simplify";
        var workingdir = fileService.getWorkingDirectory();

        // When
        fromBpmnToBlocks.createBlocksFromBpmn(process);

        // Then
        var expectedBlock = readBlocksFromFile(workingdir.resolve("expectedBlocks").resolve(process).resolve(process + ".json"));
        var actualBlocks = readBlocksFromFile(fileService.getBlocksFile(process));
        assertBlocksEqualIgnoringId(actualBlocks, expectedBlock);
    }
}
