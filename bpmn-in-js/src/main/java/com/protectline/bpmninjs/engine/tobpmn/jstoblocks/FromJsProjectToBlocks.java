package com.protectline.bpmninjs.engine.tobpmn.jstoblocks;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.engine.tobpmn.spi.BlockFromJsNode;
import com.protectline.bpmninjs.engine.tobpmn.spi.UpdateBlock;
import com.protectline.bpmninjs.model.block.Block;
import com.protectline.bpmninjs.engine.files.FileService;
import com.protectline.bpmninjs.model.jsproject.api.JsNode;
import com.protectline.bpmninjs.model.jsproject.api.JsProject;
import com.protectline.bpmninjs.model.jsproject.JsProjectImpl;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.model.block.persist.BlockUtil.readBlocksFromFile;
import static com.protectline.bpmninjs.model.block.persist.BlockUtil.writeBlocksToFile;

public class FromJsProjectToBlocks {
    private final FileService fileService;
    private final MainFactory mainFactory;

    public FromJsProjectToBlocks(FileService fileService, MainFactory mainFactory) {
        this.fileService = fileService;
        this.mainFactory = mainFactory;
    }

    public void updateBlockFromJsProject(String process) throws IOException {
        List<Block> blocksFromFile = readBlocksFromFile(fileService.getBlocksFile(process));

        JsProject jsProject = new JsProjectImpl(process, fileService, mainFactory);
        List<JsNode> elementsFromJsProject = jsProject.getElements();
        
        List<Block> blocksFromJsProject = convertElementsToBlocks(elementsFromJsProject);

        checkBlocksAreSimilar(blocksFromFile, blocksFromJsProject);

        for (Block block : blocksFromJsProject) {
            UpdateBlock updater = mainFactory.blockUpdaterFromJsFactory(block.getType());
            updater.update(blocksFromFile, block);
        }

        writeBlocksToFile(blocksFromFile, fileService.getBlocksFile(process));
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
                BlockFromJsNode parser = mainFactory.getBlockBuilder(element.getElementName());
                BlockFromJsElementResult result = parser.parse(element.getContent(), element.getAttributes());
                allBlocks.addAll(result.getBlocks());
            } catch (Exception e) {
                throw new IllegalArgumentException("No parser found for element: " + element.getElementName());
            }
        }
        
        return allBlocks;
    }
}
