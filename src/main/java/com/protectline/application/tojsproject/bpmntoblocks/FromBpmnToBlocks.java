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

        // Convertir tous les blocs en FunctionBlock
        List<FunctionBlock> functionBlocks = blocks.stream()
                .map(block -> (FunctionBlock) block)
                .toList();

        // Créer le répertoire et écrire tous les blocs dans un seul fichier JSON
        Path blockDirectory = workingDirectory.resolve("blocks");
        Files.createDirectories(blockDirectory);
        Path blocksFile = blockDirectory.resolve(process + ".json");
        writeBlocksToFile(functionBlocks, blocksFile);
    }
}
