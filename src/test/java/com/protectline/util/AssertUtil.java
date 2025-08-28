package com.protectline.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.protectline.util.DirectoryComparisonUtil.areDirectoriesEqual;
import static com.protectline.util.FileUtil.logBpmnRunnerDiff;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertUtil {

    public static void assertJsProjectIsEqualToExpected(com.protectline.files.FileUtil fileUtil, String process) throws IOException {
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
}
