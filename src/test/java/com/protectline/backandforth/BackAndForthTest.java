package com.protectline.backandforth;

import com.protectline.application.tobpmn.JsProjectToBpmn;
import com.protectline.application.tojsproject.BpmnToJS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.protectline.util.FileUtil.compareBpmnFiles;
import static com.protectline.util.FileUtil.copyDirectory;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackAndForthTest {

    @TempDir
    private Path tempDir;
    private Path testWorkingDirectory;
    private Path inputPath;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = Path.of(Objects.requireNonNull(
                BackAndForthTest.class.getClassLoader().getResource("backAndForth")).toURI());

        testWorkingDirectory = tempDir.resolve("testData");
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);

        inputPath = testWorkingDirectory.resolve("input");
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

            BpmnToJS bpmnToJs = new BpmnToJS(testWorkingDirectory);
            bpmnToJs.createProject(processName);

            // Vérifier que le projet JS a été créé
            Path outputDir = testWorkingDirectory.resolve("output").resolve(processName);
            assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
            assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

            JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(testWorkingDirectory);
            jsProjectToBpmn.updateBpmn(processName);

            assertTrue(Files.exists(bpmnFile.toPath()),
                    "BPMN file should be recreated: " + bpmnFile.getName());

            Path backupfile = testWorkingDirectory.resolve("backup").resolve(processName);
            compareBpmnFiles(bpmnFile.toPath(), backupfile);
        }
    }

    @Test
    void should_create_jsProject_and_create_bpmn_back_again() throws IOException {
        var fileName = "tus.prc.actionCombine.bpmn";

        var bpmnFile = inputPath.resolve(fileName);
        String processName = bpmnFile.getFileName().toString().replace(".bpmn", "");

        BpmnToJS bpmnToJs = new BpmnToJS(testWorkingDirectory);
        bpmnToJs.createProject(processName);

        // Vérifier que le projet JS a été créé
        Path outputDir = testWorkingDirectory.resolve("output").resolve(processName);
        assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
        assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(testWorkingDirectory);
        jsProjectToBpmn.updateBpmn(processName);

        assertTrue(Files.exists(bpmnFile),
                "BPMN file should be recreated: " + bpmnFile.getFileName().toString());

      Path backupfile = testWorkingDirectory.resolve("backup").resolve(processName);
            compareBpmnFiles(bpmnFile, backupfile);
    }
}
