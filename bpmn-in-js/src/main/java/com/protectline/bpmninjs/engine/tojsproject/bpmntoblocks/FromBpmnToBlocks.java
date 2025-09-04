package com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.engine.tojsproject.spi.BlockFromBpmnNode;
import com.protectline.bpmninjs.model.block.persist.BlockUtil;
import com.protectline.bpmninjs.model.bpmndocument.api.BpmnDocument;
import com.protectline.bpmninjs.model.bpmndocument.homemadeimpl.HomemadeBpmnDocument;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.files.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FromBpmnToBlocks {
    private final FileService fileService;
    private final List<BlockFromBpmnNode> blockFromBpmnNodes;
    private final BlockUtil blockUtil;

    public FromBpmnToBlocks(FileService fileService, List<BlockFromBpmnNode> blockFromBpmnNodes, BlockUtil blockUtil) throws IOException {
        this.fileService = fileService;
        this.blockFromBpmnNodes = blockFromBpmnNodes;
        this.blockUtil = blockUtil;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new HomemadeBpmnDocument(fileService.getBpmnFile(process).toFile());

        List<Block> blocks = new MainBlockFromBpmnNode().
                registerSubBlockBuilders(blockFromBpmnNodes)
                .getBlocks(document);

        Path blocksFile = fileService.getBlocksFile(process);
        Files.createDirectories(blocksFile.getParent());
        blockUtil.writeBlocksToFile(blocks, blocksFile);
    }
}
