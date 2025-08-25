package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.application.tobpmn.jstoblocks.BlockUpdaterFromJsFactory.getUpdater;

public class FromJsProjectToBlocks {
    private final FileUtil fileUtil;

    public FromJsProjectToBlocks(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void updateBlockFromJsProject(String process) throws IOException {
        List<Block> blocksFromFile = FunctionJsonBlockUtil.readBlocksFromFile(fileUtil.getBlocksFile(process));

        JsProject jsProject = new JsProject(fileUtil);
        List<Block> blocksFromJsProject = jsProject.getBlocks(process);

        checkBlocksAreSimilar(blocksFromFile, blocksFromJsProject);

        for (Block blockFromJs : blocksFromJsProject) {
            BlockUpdaterFromJs updater = getUpdater(blockFromJs.getType());
            updater.updateBlockContent(blocksFromFile, blockFromJs);
        }

        FunctionJsonBlockUtil.writeBlocksToFile(blocksFromFile.stream()
                .map(block -> (com.protectline.bpmninjs.common.block.FunctionBlock) block)
                .toList(), fileUtil.getBlocksFile(process));
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
