package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.common.block.Block;
import com.protectline.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FromBpmnToBlock {
    private final Path workingDirectory;

    public FromBpmnToBlock(Path workingDirectory) throws IOException {
        this.workingDirectory = workingDirectory;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(workingDirectory, process);
        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilder(new FunctionBlockBuilder())
                .getBlocks(document);

        // persist blocks
    }
}
