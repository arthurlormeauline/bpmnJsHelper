package com.protectline.tojsproject;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.jsproject.JsProject;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.functionblock.FunctionBlockBuilder;

import java.nio.file.Path;
import java.util.List;

public class BpmnToJS {

    private final Path workingDirectory;

    public BpmnToJS(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void createProject(String process) {
        BpmnDocument bpmnDocument = new BpmnDocument(workingDirectory, process);

        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilder(new FunctionBlockBuilder())
                .getBlocks(bpmnDocument);

        JsProject jsProject = new JsProject(workingDirectory, process);
        jsProject.updateProject(blocks);
    }
}
