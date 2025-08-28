package com.protectline.files;

import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileUtil {

    private final Path workingDirectory;

    public FileUtil(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public Path getBpmnFile(String processName) {
        return workingDirectory.resolve("input").resolve(processName + ".bpmn");
    }

    public Path getBlocksFile(String process) {
        return workingDirectory.resolve("blocks").resolve(process).resolve(process + ".json");
    }

    public Path getJsProjectDirectory(String processName) {
        return workingDirectory.resolve("output").resolve(processName);
    }

    public Path getBpmnDirectory() {
        return workingDirectory.resolve("input");
    }

    public Path getTemplateDirectory() {
        return workingDirectory.resolve("template");
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

}
