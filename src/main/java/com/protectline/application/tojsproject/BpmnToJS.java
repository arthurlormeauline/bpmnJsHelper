package com.protectline.application.tojsproject;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.jsproject.JsProject;
import com.protectline.common.block.Block;
import com.protectline.translate.frombpmntoblock.MainBlockBuilder;
import com.protectline.translate.frombpmntoblock.functionblock.FunctionBlockBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BpmnToJS {

    private final Path workingDirectory;

    public BpmnToJS(Path workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    public void createProject(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(workingDirectory, process);

        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilder(new FunctionBlockBuilder())
                .getBlocks(document);

        JsProject jsProject = new JsProject(workingDirectory, process);
        jsProject.updateProject(blocks);
    }
}
