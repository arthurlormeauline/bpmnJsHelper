package com.protectline.tojsproject;

import com.protectline.tojsproject.blockbuilder.BlockBuilder;
import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.JsProject;
import com.protectline.jsproject.model.block.Block;

import java.nio.file.Path;
import java.util.List;

public class BpmnToJS {

    private final Path workingDirectory;

    public BpmnToJS(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void createProject(String process) {
        BpmnDocument bpmnDocument = new BpmnDocument(workingDirectory, process);
        BlockBuilder blockBuilder = new BlockBuilder(bpmnDocument);

        List<Block> blocks = blockBuilder.buildAllBlocks();
        JsProject jsProject = new JsProject(workingDirectory, process);
        jsProject.updateProject(blocks);
    }
}
