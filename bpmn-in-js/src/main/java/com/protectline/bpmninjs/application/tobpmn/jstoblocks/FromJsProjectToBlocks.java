package com.protectline.bpmninjs.application.tobpmn.jstoblocks;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.application.tobpmn.spi.BlockFromElement;
import com.protectline.bpmninjs.application.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.application.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsNode;
import com.protectline.bpmninjs.jsproject.JsProject;
import com.protectline.bpmninjs.jsprojectimpl.JsProjectImpl;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.common.block.persist.BlockUtil.readBlocksFromFile;
import static com.protectline.bpmninjs.common.block.persist.BlockUtil.writeBlocksToFile;

public class FromJsProjectToBlocks {
    private final FileUtil fileUtil;
    private final MainFactory mainFactory;

    public FromJsProjectToBlocks(FileUtil fileUtil, MainFactory mainFactory) {
        this.fileUtil = fileUtil;
        this.mainFactory = mainFactory;
    }

    public void updateBlockFromJsProject(String process) throws IOException {
        List<Block> blocksFromFile = readBlocksFromFile(fileUtil.getBlocksFile(process));

        JsProject jsProject = new JsProjectImpl(process, fileUtil, mainFactory);
        List<JsNode> elementsFromJsProject = jsProject.getElements();
        
        List<Block> blocksFromJsProject = convertElementsToBlocks(elementsFromJsProject);

        checkBlocksAreSimilar(blocksFromFile, blocksFromJsProject);

        for (Block block : blocksFromJsProject) {
            UpdateBlock updater = mainFactory.blockUpdaterFromJsFactory(block.getType());
            updater.updateBlockContent(blocksFromFile, block);
        }

        writeBlocksToFile(blocksFromFile, fileUtil.getBlocksFile(process));
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

    private List<Block> convertElementsToBlocks(List<JsNode> elements) {
        List<Block> allBlocks = new java.util.ArrayList<>();

        for (JsNode element : elements) {
            try {
                BlockFromElement parser = mainFactory.getBlockBuilder(element.getElementName());
                BlockFromElementResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                throw new IllegalArgumentException("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }
}
