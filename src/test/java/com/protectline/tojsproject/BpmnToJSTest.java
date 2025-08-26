package com.protectline.tojsproject;

import com.protectline.tobpmn.JsProjectToBpmn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BpmnToJSTest {

    @TempDir
    Path tempDir;

    private Path backupPath;
    private Path testWorkingDirectory;
    private String testFolderName;
    private Path resourcePath;
    private Path inputPath;

    BpmnToJSTest() throws URISyntaxException {
        testFolderName = "tojsprojectTestData";

        resourcePath = Path.of(Objects.requireNonNull(
                BpmnToJSTest.class.getClassLoader().getResource(testFolderName)).toURI()).getParent();

        testWorkingDirectory = resourcePath.resolve(testFolderName);

        inputPath = testWorkingDirectory.resolve("input");
    }

    @BeforeEach
    void setUp() throws Exception {
        backupPath = tempDir.resolve("backup");
        Files.createDirectories(backupPath);
    }

    @Test
    void should_create_jsProject_and_create_bpmn_back_again_for_all_files() throws IOException {
        File[] bpmnFiles = inputPath.toFile().listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".bpmn"));

        if (bpmnFiles == null || bpmnFiles.length == 0) {
            throw new RuntimeException("No BPMN files found in input resources");
        }

        for (File bpmnFile : bpmnFiles) {
            String processName = bpmnFile.getName().replace(".bpmn", "");

            Path backupFile = backupPath.resolve(bpmnFile.getName());
            Files.copy(bpmnFile.toPath(), backupFile, StandardCopyOption.REPLACE_EXISTING);

            BpmnToJS bpmnToJs = new BpmnToJS(testWorkingDirectory);
            bpmnToJs.createProject(processName);

            Files.delete(bpmnFile.toPath());

            JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(testWorkingDirectory);
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

    @Test
    void should_create_jsProject_and_create_bpmn_back_again() throws IOException {

        var fileName = "tus.prc.actionCombine.bpmn";

        var bpmnFile = inputPath.resolve(fileName);
        String processName = bpmnFile.getFileName().toString().replace(".bpmn", "");

        Path backupFile = backupPath.resolve(bpmnFile.getFileName().toString());
        Files.copy(bpmnFile, backupFile, StandardCopyOption.REPLACE_EXISTING);

        BpmnToJS bpmnToJs = new BpmnToJS(testWorkingDirectory);
        bpmnToJs.createProject(processName);

        Files.delete(bpmnFile);

        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(testWorkingDirectory);
        jsProjectToBpmn.updateBpmn(processName);

        assertTrue(Files.exists(bpmnFile),
                "BPMN file should be recreated: " + bpmnFile.getFileName().toString());

        byte[] originalContent = Files.readAllBytes(backupFile);
        byte[] recreatedContent = Files.readAllBytes(bpmnFile);

        assertTrue(java.util.Arrays.equals(originalContent, recreatedContent),
                "Recreated BPMN file should match original: " + bpmnFile.getFileName().toString());

        Files.copy(backupFile, bpmnFile, StandardCopyOption.REPLACE_EXISTING);
    }
}
