package com.protectline.bpmninjs.engine.files;

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

    public Path getBpmnDirectory() {
        return workingDirectory.resolve(INPUT);
    }


    public void deleteJsDirectoryIfExists(String  process) throws IOException {
        var directory = getJsProjectDirectory(process);
        if (Files.exists(directory)) {
            FileUtils.deleteDirectory(directory.toFile());
        }
    }
}
