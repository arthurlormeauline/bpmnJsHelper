package com.protectline.bpmninjs.application.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;
import com.protectline.bpmninjs.jsproject.JsProjectImpl;
import com.protectline.bpmninjs.jsproject.JsUpdater;

import java.io.IOException;
import java.util.List;

public class FromBlockToJsProject {

    private final FileUtil fileUtil;
    private final BlockWriter blockWriter;
    private final MainFactory mainFactory;

    public FromBlockToJsProject(FileUtil fileUtil, BlockWriter blockWriter, MainFactory mainFactory) throws IOException {
        this.fileUtil = fileUtil;
        this.blockWriter = blockWriter;
        this.mainFactory = mainFactory;
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        List<Block> blocks = blockWriter.readBlocksFromFile(fileUtil.getBlocksFile(process));
        
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
