package com.protectline.bpmninjs.backandforth;

import com.protectline.bpmninjs.util.MainFactoryTestUtil;
import com.protectline.bpmninjs.engine.tobpmn.JsProjectToBpmn;
import com.protectline.bpmninjs.engine.tojsproject.BpmnToJsProject;
import com.protectline.bpmninjs.engine.files.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static com.protectline.bpmninjs.util.FileUtil.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackAndForthTest {

    @TempDir
    private Path tempDir;
    private FileService fileService;

    @BeforeEach
    void setUp() throws Exception {
        // Copier toute la structure de test vers le répertoire temporaire
        String testDirectory = "backAndForth";
        Path resourcesPath = getResourcePath(BackAndForthTest.class, testDirectory);

        var testWorkingDirectory = tempDir.resolve("testData");
        Files.createDirectories(testWorkingDirectory);

        // Copier récursivement toute la structure
        copyDirectory(resourcesPath, testWorkingDirectory);
        
        // Créer le dossier backup et copier les fichiers originaux pour comparaison
        Path backupDir = testWorkingDirectory.resolve("backup");
        Files.createDirectories(backupDir);
        Path inputDir = testWorkingDirectory.resolve("input");
        
        // Copier tous les fichiers .bpmn de input vers backup
        try (var stream = Files.list(inputDir)) {
            stream.filter(path -> path.toString().toLowerCase().endsWith(".bpmn"))
                  .forEach(bpmnFile -> {
                      try {
                          Path backupFile = backupDir.resolve(bpmnFile.getFileName());
                          Files.copy(bpmnFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
                      } catch (IOException e) {
                          throw new RuntimeException("Failed to copy backup file: " + bpmnFile, e);
                      }
                  });
        }
        
        this.fileService = new FileService(testWorkingDirectory);
    }


    @Test
    void should_create_jsProject_and_create_bpmn_back_again_for_all_files() throws IOException {
        var inputPath = fileService.getBpmnDirectory();
        var workDir = fileService.getWorkingDirectory();

        File[] bpmnFiles = inputPath.toFile().listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".bpmn"));

        if (bpmnFiles == null || bpmnFiles.length == 0) {
            throw new RuntimeException("No BPMN files found in input resources");
        }

        for (File bpmnFile : bpmnFiles) {
            String processName = bpmnFile.getName().replace(".bpmn", "");

            BpmnToJsProject bpmnToJsProject = new BpmnToJsProject(fileService, MainFactoryTestUtil.createWithDefaults(fileService));
            bpmnToJsProject.createProject(processName);

            // Vérifier que le projet JS a été créé
            Path outputDir = fileService.getJsProjectDirectory(processName);
            assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
            assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

            JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(fileService, MainFactoryTestUtil.createWithDefaults(fileService));
            jsProjectToBpmn.updateBpmn(processName);

            assertTrue(Files.exists(bpmnFile.toPath()),
                    "BPMN file should be recreated: " + bpmnFile.getName());

            Path backupfile = workDir.resolve("backup").resolve(processName+".bpmn");
            compareBpmnFiles(bpmnFile.toPath(), backupfile);
        }
    }


    @Test
    void should_create_jsProject_and_create_bpmn_back_again() throws IOException {
        var process = "CreateCustomer_Dev";
        var bpmnFile = fileService.getBpmnFile(process);
        var workDir = fileService.getWorkingDirectory();

        BpmnToJsProject bpmnToJsProject = new BpmnToJsProject(fileService, MainFactoryTestUtil.createWithDefaults(fileService));
        bpmnToJsProject.createProject(process);

        // Vérifier que le projet JS a été créé
        Path outputDir = fileService.getJsProjectDirectory(process);
        assertTrue(Files.exists(outputDir), "JS project directory should exist: " + outputDir);
        assertTrue(Files.list(outputDir).findAny().isPresent(), "JS project directory should not be empty: " + outputDir);

        JsProjectToBpmn jsProjectToBpmn = new JsProjectToBpmn(fileService, MainFactoryTestUtil.createWithDefaults(fileService));
        jsProjectToBpmn.updateBpmn(process);

        assertTrue(Files.exists(bpmnFile),
                "BPMN file should be recreated: " + bpmnFile.getFileName().toString());

        Path backupfile = workDir.resolve("backup").resolve(process+".bpmn");
        compareBpmnFiles(bpmnFile, backupfile);
    }
}
