package com.protectline.tojsproject;

import com.protectline.tobpmn.JsProjectToBpmn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BpmnToJSTest {

    @TempDir
    Path tempDir;

    private Path inputResourcesPath;
    private Path backupPath;

    @BeforeEach
    void setUp() throws Exception {
        inputResourcesPath = Path.of(Objects.requireNonNull(
            getClass().getClassLoader().getResource("tojsprojectTestData/input")).toURI());
        
        backupPath = tempDir.resolve("backup");
        Files.createDirectories(backupPath);
    }

    @Test
    void should_create_jsProject_and_create_bpmn_back_again() throws IOException {
        Path resourcesPath = inputResourcesPath.getParent();
        
        File[] bpmnFiles = inputResourcesPath.toFile().listFiles(
            (dir, name) -> name.toLowerCase().endsWith(".bpmn"));
        
        if (bpmnFiles == null || bpmnFiles.length == 0) {
            throw new RuntimeException("No BPMN files found in input resources");
        }

        for (File bpmnFile : bpmnFiles) {
            String processName = bpmnFile.getName().replace(".bpmn", "");
            
            Path backupFile = backupPath.resolve(bpmnFile.getName());
            Files.copy(bpmnFile.toPath(), backupFile, StandardCopyOption.REPLACE_EXISTING);
            
            BpmnToJS bpmnToJs = new BpmnToJS(resourcesPath);
            bpmnToJs.createProject(processName);
            
            Files.delete(bpmnFile.toPath());

            JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(resourcesPath);
            jsProjectToBpmn.updateBpmn(processName);
            
            assertTrue(Files.exists(bpmnFile.toPath()),
                "BPMN file should be recreated: " + bpmnFile.getName());
            
            byte[] originalContent = Files.readAllBytes(backupFile);
            byte[] recreatedContent = Files.readAllBytes(bpmnFile.toPath());
            
            assertTrue(java.util.Arrays.equals(originalContent, recreatedContent),
                "Recreated BPMN file should match original: " + bpmnFile.getName());
            
            Files.copy(backupFile, bpmnFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
