package com.protectline.bpmninjs.application.files;

import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileUtil {

    private static final String INPUT = "input";
    private static final String BLOCKS = "blocks";
    private static final String OUTPUT = "output";
    private static final String JS_TEMPLATE = "template";
    private static final String UPDATER_TEMPLATE= "jsupdatertemplates";

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    private final Path workingDirectory;

    public FileUtil(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public Path getBpmnFile(String processName) {
        return workingDirectory.resolve(INPUT).resolve(processName + ".bpmn");
    }

    public Path getBlocksFile(String process) {
        return workingDirectory.resolve(BLOCKS).resolve(process).resolve(process + ".json");
    }

    public Path getJsProjectDirectory(String processName) {
        return workingDirectory.resolve(OUTPUT).resolve(processName);
    }
    public String getJsRunnerFileContent(String process) throws IOException {
       return Files.readString(getJsProjectDirectory(process).resolve("BpmnRunner.js"));
    }

    public Path getUpdaterTemplateDirectory(String processName) {
        return workingDirectory.resolve(UPDATER_TEMPLATE);
    }

    public Path getBpmnDirectory() {
        return workingDirectory.resolve(INPUT);
    }

    public Path getTemplateDirectory() {
        return workingDirectory.resolve(JS_TEMPLATE);
    }

    public void deleteJsDirectoryIfExists(String  process) throws IOException {
        var directory = getJsProjectDirectory(process);
        if (Files.exists(directory)) {
            FileUtils.deleteDirectory(directory.toFile());
        }
    }

    public void copyTemplateToJsDirectory(String process) throws IOException {
        var templateSource= getTemplateDirectory();
        var destination = getJsProjectDirectory(process);
        FileUtils.copyDirectory(templateSource.toFile(), destination.toFile());
    }

    public Path getJsUpdaterTemplatesJsonFile() {
        return workingDirectory.resolve("jsupdatertemplates/jsupdatertemplates.json");
    }
}
