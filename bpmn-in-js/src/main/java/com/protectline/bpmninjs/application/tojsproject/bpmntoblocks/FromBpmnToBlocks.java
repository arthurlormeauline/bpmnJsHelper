package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.application.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.bpmndocument.BpmnDocument;
import com.protectline.bpmninjs.camundbpmnaparser.BpmnCamundaDocument;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FromBpmnToBlocks {
    private final FileUtil fileUtil;
    private final List<BlockBuilder> blockBuilders;
    private final BlockWriter blockWriter;

    public FromBpmnToBlocks(FileUtil fileUtil, List<BlockBuilder> blockBuilders, BlockWriter blockWriter) throws IOException {
        this.fileUtil = fileUtil;
        this.blockBuilders = blockBuilders;
        this.blockWriter = blockWriter;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(fileUtil.getBpmnFile(process).toFile());

        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilders(blockBuilders)
                .getBlocks(document);

        Path blocksFile = fileUtil.getBlocksFile(process);
        Files.createDirectories(blocksFile.getParent());
        blockWriter.writeBlocksToFile(blocks, blocksFile);
    }
}
