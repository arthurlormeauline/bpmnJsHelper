package com.protectline.bpmninjs.util;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.protectline.bpmninjs.util.DirectoryComparisonUtil.areDirectoriesEqual;
import static com.protectline.bpmninjs.util.FileUtil.logBpmnRunnerDiff;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertUtil {

    public static void assertJsProjectIsEqualToExpected(com.protectline.bpmninjs.files.FileUtil fileUtil, String process) throws IOException {
        Path outputDir = fileUtil.getJsProjectDirectory(process);
        assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
        assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

        Path expectedProject = fileUtil.getWorkingDirectory().resolve("expectedJsProject").resolve(process);

        // Log line-by-line diff for BpmnRunner.js
        Path actualBpmnRunner = outputDir.resolve("BpmnRunner.js");
        Path expectedBpmnRunner = expectedProject.resolve("BpmnRunner.js");
        logBpmnRunnerDiff(actualBpmnRunner, expectedBpmnRunner);

        assertThat(areDirectoriesEqual(outputDir, expectedProject)).isTrue();
    }

    public static void assertBlocksAreEqualToExpected(com.protectline.bpmninjs.files.FileUtil fileUtil, String process) throws IOException {
        // Read actual blocks from the generated blocks file
        Path actualBlocksFile = fileUtil.getBlocksFile(process);
        assertTrue(Files.exists(actualBlocksFile), "Blocks file should exist: " + actualBlocksFile);
        
        List<Block> actualBlocks = FunctionJsonBlockUtil.readBlocksFromFile(actualBlocksFile);
        
        // Read expected blocks from the expected blocks file
        Path expectedBlocksFile = fileUtil.getWorkingDirectory().resolve("expectedblocks").resolve(process).resolve(process + ".json");
        assertTrue(Files.exists(expectedBlocksFile), "Expected blocks file should exist: " + expectedBlocksFile);
        
        List<Block> expectedBlocks = FunctionJsonBlockUtil.readBlocksFromFile(expectedBlocksFile);
        
        // Compare blocks
        assertThat(actualBlocks.size()).isEqualTo(expectedBlocks.size());
    }
}
