package com.protectline.jsproject;

import com.protectline.common.block.Block;

import java.nio.file.Path;
import java.util.List;

public class JsProject {

    private final Path workingDirectory;


    public JsProject(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void updateProject(String process, List<Block> blocks) {

    }

    public List<Block> getBlocks() {
        return null;
    }
}
