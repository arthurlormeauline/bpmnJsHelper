package com.protectline.files;

import lombok.Getter;

import java.io.File;
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

}
