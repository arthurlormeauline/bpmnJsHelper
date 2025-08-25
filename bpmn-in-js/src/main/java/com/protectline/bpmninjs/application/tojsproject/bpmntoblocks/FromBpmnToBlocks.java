package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.functionblock.FunctionBlockBuilder;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.FunctionBlock;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil.writeBlocksToFile;

public class FromBpmnToBlocks {
    private final FileUtil fileUtil;

    public FromBpmnToBlocks(FileUtil fileUtil) throws IOException {
        this.fileUtil = fileUtil;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(fileUtil.getBpmnFile(process).toFile());
        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilder(new FunctionBlockBuilder())
                .getBlocks(document);

        // Convertir tous les blocs en FunctionBlock
        List<FunctionBlock> functionBlocks = blocks.stream()
                .map(block -> (FunctionBlock) block)
                .toList();

        // Créer le répertoire et écrire tous les blocs dans un seul fichier JSON
        Path blocksFile = fileUtil.getBlocksFile(process);
        Files.createDirectories(blocksFile.getParent());
        writeBlocksToFile(functionBlocks, blocksFile);
    }
}
