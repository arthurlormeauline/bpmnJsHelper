package com.protectline.tojsproject;

import com.protectline.tojsproject.model.block.Block;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class BpmnToJS {

    private final Path workingDirectory;

    public BpmnToJS(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void createProject(String process) {
        ElementSelector elementSelector = new ElementSelector();
        BlockBuilder blockBuilder = new BlockBuilder (bpmnSelector);

        List<Block> blocks = blockBuilder.buildBlocks();
        JsProject jsProject = new JsProject();
        jsProject.updateProject(blocks);
    }
}
