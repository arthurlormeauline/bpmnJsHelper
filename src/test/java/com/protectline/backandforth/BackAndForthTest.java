package com.protectline.backandforth;

import com.protectline.application.tobpmn.JsProjectToBpmn;
import com.protectline.application.tojsproject.BpmnToJS;
import com.protectline.files.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static com.protectline.util.FileUtil.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackAndForthTest {

    @TempDir
    private Path tempDir;
    private FileUtil fileUtil;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        Path resourcesPath = getResourcePath(BackAndForthTest.class, "backAndForth");

        var testWorkingDirectory = tempDir.resolve("testData");
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
        this.fileUtil = new FileUtil(testWorkingDirectory);
    }



    @Test
    void should_create_jsProject_and_create_bpmn_back_again_for_all_files() throws IOException {
        var inputPath = fileUtil.getBpmnDirectory();
        var workDir = fileUtil.getWorkingDirectory();

        File[] bpmnFiles = inputPath.toFile().listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".bpmn"));

        if (bpmnFiles == null || bpmnFiles.length == 0) {
            throw new RuntimeException("No BPMN files found in input resources");
        }

        for (File bpmnFile : bpmnFiles) {
            String processName = bpmnFile.getName().replace(".bpmn", "");

            BpmnToJS bpmnToJs = new BpmnToJS(workDir);
            bpmnToJs.createProject(processName);

            // Vérifier que le projet JS a été créé
            Path outputDir = fileUtil.getJsProjectDirectory(processName);
            assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
            assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

            JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(workDir);
            jsProjectToBpmn.updateBpmn(processName);

            assertTrue(Files.exists(bpmnFile.toPath()),
                    "BPMN file should be recreated: " + bpmnFile.getName());

            Path backupfile = workDir.resolve("backup").resolve(processName);
            compareBpmnFiles(bpmnFile.toPath(), backupfile);
        }
    }


    @Test
    void should_create_jsProject_and_create_bpmn_back_again() throws IOException {
        var process = "tus.prc.actionCombine";
        var bpmnFile = fileUtil.getBpmnFile(process);
        var workDir = fileUtil.getWorkingDirectory();

        BpmnToJS bpmnToJs = new BpmnToJS(workDir);
        bpmnToJs.createProject(process);

        // Vérifier que le projet JS a été créé
        Path outputDir = fileUtil.getJsProjectDirectory(process);
        assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
        assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(workDir);
        jsProjectToBpmn.updateBpmn(process);

        assertTrue(Files.exists(bpmnFile),
                "BPMN file should be recreated: " + bpmnFile.getFileName().toString());

      Path backupfile = workDir.resolve("backup").resolve(process);
            compareBpmnFiles(bpmnFile, backupfile);
    }
}
