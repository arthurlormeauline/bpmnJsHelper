package com.protectline.bpmninjs.engine.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.model.common.block.persist.BlockUtil;
import com.protectline.bpmninjs.model.common.block.Block;
import com.protectline.bpmninjs.engine.files.FileUtil;
import com.protectline.bpmninjs.model.jsproject.api.JsProject;
import com.protectline.bpmninjs.model.jsproject.JsProjectImpl;
import com.protectline.bpmninjs.engine.tojsproject.spi.JsUpdater;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.model.common.block.persist.BlockUtil.readBlocksFromFile;

public class FromBlockToJsProject {

    private final FileUtil fileUtil;
    private final BlockUtil blockUtil;
    private final MainFactory mainFactory;

    public FromBlockToJsProject(FileUtil fileUtil, BlockUtil blockUtil, MainFactory mainFactory) throws IOException {
        this.fileUtil = fileUtil;
        this.blockUtil = blockUtil;
        this.mainFactory = mainFactory;
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        List<Block> blocks = readBlocksFromFile(fileUtil.getBlocksFile(process));
        
        fileUtil.deleteJsDirectoryIfExists(process);
        fileUtil.copyTemplateToJsDirectory(process);
        
        JsProject jsProject = new JsProjectImpl(process, fileUtil, mainFactory);
        
        String jsContent = jsProject.getJsContent();
        var updaters = mainFactory.getJsUpdaters(blocks);
        
        String updatedContent = jsContent;
        for (JsUpdater updater : updaters) {
            updatedContent = updater.update(updatedContent, blocks);
        }
        
        jsProject.writeJsContent(updatedContent);
    }
}
