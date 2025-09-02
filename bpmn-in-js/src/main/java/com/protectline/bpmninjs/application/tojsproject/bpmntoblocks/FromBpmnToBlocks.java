package com.protectline.bpmninjs.application.tojsproject.bpmntoblocks;

import com.protectline.bpmninjs.application.WriteBlock;
import com.protectline.bpmninjs.application.BlockWriterFactory;
import com.protectline.bpmninjs.application.BuildersProvider;
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
    private final BuildersProvider builderProvider;
    private BlockWriterFactory blockWriterFactory;

    public FromBpmnToBlocks(FileUtil fileUtil, BuildersProvider buildersProvider, BlockWriterFactory blockWriterFactory) throws IOException {
        this.fileUtil = fileUtil;
        this.builderProvider= buildersProvider;
        this.blockWriterFactory = blockWriterFactory;
    }

    public void createBlocksFromBpmn(String process) throws IOException {
        BpmnDocument document = new BpmnCamundaDocument(fileUtil.getBpmnFile(process).toFile());

        List<Block> blocks = new MainBlockBuilder().
                registerSubBlockBuilders(builderProvider.getBuilders())
                .getBlocks(document);

        Path blocksFile = fileUtil.getBlocksFile(process);
        Files.createDirectories(blocksFile.getParent());
        WriteBlock writer = null;

        for (Block block : blocks){
            writer = blockWriterFactory.getBlockWriter(block);
        }

        writer.writeBlocksToFile(blocks, blocksFile);
    }
}
