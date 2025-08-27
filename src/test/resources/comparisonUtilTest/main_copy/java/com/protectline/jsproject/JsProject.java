package com.protectline.jsproject;

import com.protectline.common.block.Block;

import java.nio.file.Path;
import java.util.List;

public class JsProject {

    private final Path workingDirectory;
    private final String process;


    public JsProject(Path workingDirectory, String process) {
        this.workingDirectory = workingDirectory;
        this.process = process;
    }

    public void updateProject(List<Block> blocks) {

    }
}
