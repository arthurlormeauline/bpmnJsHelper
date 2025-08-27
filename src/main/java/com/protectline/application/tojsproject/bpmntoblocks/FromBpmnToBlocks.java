package com.protectline.application.tojsproject.bpmntoblocks;

import com.protectline.bpmndocument.BpmnDocument;
import com.protectline.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.common.block.Block;
import com.protectline.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;
import com.protectline.common.block.FunctionBlock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.protectline.common.block.jsonblock.FunctionJsonBlockUtil.writeBlocksToFile;

public class FromBpmnToBlocks {
    private final Path workingDirectory;

    public FromBpmnToBlocks(Path workingDirectory) throws IOException {
        this.workingDirectory = workingDirectory;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(workingDirectory, process);
        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilder(new FunctionBlockBuilder())
                .getBlocks(document);

        blocks.forEach(block -> {
            try {
                Path blockDirectory = workingDirectory.resolve("blocks/" + process);
                Files.createDirectories(blockDirectory);
                Path blockFile = blockDirectory.resolve(block.getName() + ".json");
                writeBlocksToFile((FunctionBlock) block, blockFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
