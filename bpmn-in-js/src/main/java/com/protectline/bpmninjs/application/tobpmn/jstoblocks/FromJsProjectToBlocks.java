package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;
import com.protectline.bpmninjs.jsproject.JsProjectImpl;

import java.io.IOException;
import java.util.List;

public class FromJsProjectToBlocks {
    private final FileUtil fileUtil;
    private final MainFactory mainFactory;
    private final BlockWriter blockWriter;

    public FromJsProjectToBlocks(FileUtil fileUtil, MainFactory mainFactory) {
        this.fileUtil = fileUtil;
        this.mainFactory = mainFactory;
        this.blockWriter = new BlockWriter();
    }

    public void updateBlockFromJsProject(String process) throws IOException {
        List<Block> blocksFromFile = blockWriter.readBlocksFromFile(fileUtil.getBlocksFile(process));

        JsProject jsProject = new JsProjectImpl(process, fileUtil, mainFactory);
        List<Block> blocksFromJsProject = jsProject.getBlocks();

        checkBlocksAreSimilar(blocksFromFile, blocksFromJsProject);

        for (Block block : blocksFromJsProject) {
            UpdateBlockFromJs updater = mainFactory.blockUpdaterFromJsFactory(block.getType());
            updater.updateBlockContent(blocksFromFile, block);
        }

        blockWriter.writeBlocksToFile(blocksFromFile, fileUtil.getBlocksFile(process));
    }

    private static void checkBlocksAreSimilar(List<Block> blocksFromFile, List<Block> blocksFromJsProject) {
        if (!blockAreSimilar(blocksFromFile, blocksFromJsProject)) {
            throw new IllegalStateException("Number of blocks from file (" + blocksFromFile.size() +
                    ") does not match blocks from JS project (" + blocksFromJsProject.size() + ")");
        }
    }

    private static boolean blockAreSimilar(List<Block> blocksFromFile, List<Block> blocksFromJsProject) {
        if (blocksFromFile.size() != blocksFromJsProject.size())
            return false;
        if (!blocksFromFile.stream().map(block -> block.getId()).toList()
                .containsAll(blocksFromJsProject.stream().map(block -> block.getId()).toList())
            ||
        !blocksFromJsProject.stream().map(block -> block.getId()).toList()
                .containsAll(blocksFromFile.stream().map(block -> block.getId()).toList()))
            return false;
        return true;
    }
}
