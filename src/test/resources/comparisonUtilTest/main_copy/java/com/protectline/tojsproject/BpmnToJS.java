package com.protectline.tojsproject;

import com.protectline.bpmndocument.model.camundaadapter.BpmnCamundaDocument;
import com.protectline.jsproject.JsProject;
import com.protectline.jsproject.model.block.Block;
import com.protectline.tojsproject.block.MainBlockBuilder;

import java.nio.file.Path;
import java.util.List;

public class BpmnToJS {

    private final Path workingDirectory;

    public BpmnToJS(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void createProject(String process) {
        BpmnCamundaDocument bpmnCamundaDocument = new BpmnCamundaDocument(workingDirectory, process);
        MainBlockBuilder mainBlockBuilder = new MainBlockBuilder(bpmnCamundaDocument);

        List<Block> blocks = mainBlockBuilder.buildAllBlocks();
        JsProject jsProject = new JsProject(workingDirectory, process);
        jsProject.updateProject(blocks);
    }
}
