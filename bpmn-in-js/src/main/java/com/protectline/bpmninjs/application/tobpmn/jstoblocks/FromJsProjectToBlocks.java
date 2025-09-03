package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.application.MainProvider;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;

import java.io.IOException;
import java.util.List;

public class FromJsProjectToBlocks {
    private final FileUtil fileUtil;
    private final MainProvider mainProvider;

    public FromJsProjectToBlocks(FileUtil fileUtil, MainProvider mainProvider) {
        this.fileUtil = fileUtil;
        this.mainProvider = mainProvider;
    }

    public void updateBlockFromJsProject(String process) throws IOException {
        List<Block> blocksFromFile = mainProvider.getBlockFileUtilProvider().getBlockWriterFactory().getBlockWriter()
                .readBlocksFromFile(fileUtil.getBlocksFile(process));

        JsProject jsProject = new JsProject(fileUtil, mainProvider);
        List<Block> blocksFromJsProject = jsProject.getBlocks(process);

        checkBlocksAreSimilar(blocksFromFile, blocksFromJsProject);

        for (Block blockFromJs : blocksFromJsProject) {
            UpdateBlockFromJs updater = mainProvider.jsUpdaterFomBlockFactory(blockFromJs.getType());
            updater.updateBlockContent(blocksFromFile, blockFromJs);
        }

        mainProvider.getBlockFileUtilProvider().getBlockWriterFactory().getBlockWriter()
                .writeBlocksToFile(blocksFromFile, fileUtil.getBlocksFile(process));
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
