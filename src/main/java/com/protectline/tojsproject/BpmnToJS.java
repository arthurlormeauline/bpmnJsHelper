package com.protectline.tojsproject;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.JsProject;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.block.MainBlockBuilder;
import com.protectline.tojsproject.block.functionblock.FunctionBlockBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BpmnToJS {

    private final Path workingDirectory;

    public BpmnToJS(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void createProject(String process) throws IOException {
        BpmnCamundaDocument bpmnCamundaDocument = new BpmnCamundaDocument(workingDirectory, process);

        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilder(new FunctionBlockBuilder())
                .getBlocks(bpmnCamundaDocument);

        JsProject jsProject = new JsProject(workingDirectory, process);
        jsProject.updateProject(blocks);
    }
}
