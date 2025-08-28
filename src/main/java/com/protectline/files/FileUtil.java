package com.protectline.files;

import java.io.File;
import java.nio.file.Path;

public class FileUtil {

    public static File getBpmnFile(Path workingDirectory, String processName) {
        return workingDirectory.resolve("input").resolve(processName + ".bpmn").toFile();
    }

    public static Path getBlocksFile(Path workingDirectory, String process) {
        return workingDirectory.resolve("blocks").resolve(process).resolve(process + ".json");
    }


}
