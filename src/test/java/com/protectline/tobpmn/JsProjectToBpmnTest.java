package com.protectline.tobpmn;

import com.protectline.tojsproject.BpmnToJS;
import com.protectline.util.DirectoryComparisonUtil;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JsProjectToBpmnTest {

    @TempDir
    Path tempDir;

    private Path jsProjectOutputPath;
    private Path backupPath;

    @BeforeEach
    void setUp() throws Exception {
        jsProjectOutputPath = Path.of(Objects.requireNonNull(
            getClass().getClassLoader().getResource("tobpmnTestData/output")).toURI());
        
        backupPath = tempDir.resolve("backup");
        Files.createDirectories(backupPath);
    }

    @Test
    void should_create_bpmn_from_jsProject_and_create_jsProject_back_again() throws IOException {
        Path resourcesPath = jsProjectOutputPath.getParent();
        
        File[] jsProjectDirs = jsProjectOutputPath.toFile().listFiles(File::isDirectory);
        
        if (jsProjectDirs == null || jsProjectDirs.length == 0) {
            throw new RuntimeException("No JS project directories found in output resources");
        }

        for (File jsProjectDir : jsProjectDirs) {
            String processName = jsProjectDir.getName();
            
            Path backupDir = backupPath.resolve(processName);
            FileUtils.copyDirectory(jsProjectDir, backupDir.toFile());
            
            JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(resourcesPath);
            jsProjectToBpmn.updateBpmn(processName);
            
            FileUtils.deleteDirectory(jsProjectDir);
            
            BpmnToJS bpmnToJs = new BpmnToJS(resourcesPath);
            bpmnToJs.createProject(processName);
            
            assertTrue(jsProjectDir.exists(),
                "JS project directory should be recreated: " + processName);
            
            assertTrue(DirectoryComparisonUtil.areDirectoriesEqual(jsProjectDir.toPath(), backupDir),
                "Recreated JS project should match original: " + processName);
            
            FileUtils.deleteDirectory(jsProjectDir);
            FileUtils.copyDirectory(backupDir.toFile(), jsProjectDir);
        }
    }

}
