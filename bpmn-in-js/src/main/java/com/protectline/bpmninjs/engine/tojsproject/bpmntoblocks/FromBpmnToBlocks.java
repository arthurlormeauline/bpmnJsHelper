package com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.engine.tojsproject.spi.BlockBuilder;
import com.protectline.bpmninjs.model.common.block.persist.BlockUtil;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.camundaimpl.BpmnCamundaDocument;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.files.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FromBpmnToBlocks {
    private final FileUtil fileUtil;
    private final List<BlockBuilder> blockBuilders;
    private final BlockUtil blockUtil;

    public FromBpmnToBlocks(FileUtil fileUtil, List<BlockBuilder> blockBuilders, BlockUtil blockUtil) throws IOException {
        this.fileUtil = fileUtil;
        this.blockBuilders = blockBuilders;
        this.blockUtil = blockUtil;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(fileUtil.getBpmnFile(process).toFile());

        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilders(blockBuilders)
                .getBlocks(document);

        Path blocksFile = fileUtil.getBlocksFile(process);
        Files.createDirectories(blocksFile.getParent());
        blockUtil.writeBlocksToFile(blocks, blocksFile);
    }
}
