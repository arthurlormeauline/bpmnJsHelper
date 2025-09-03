package com.protectline.bpmninjs.jsproject;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsProjectService {

    private final FileUtil fileUtil;
    private final MainFactory mainFactory;

    public JsProjectService(FileUtil fileUtil, MainFactory mainFactory) throws IOException {
        this.fileUtil = fileUtil;
        this.mainFactory = mainFactory;
    }

    public void updateProject(JsProject jsProject, List<Block> blocks) throws IOException {
        String jsContent = jsProject.getJsContent();
        var updaters = mainFactory.getJsUpdaters(blocks);
        
        String updatedContent = jsContent;
        for (JsUpdater updater : updaters) {
            updatedContent = updater.update(updatedContent, blocks);
        }
        
        jsProject.writeJsContent(updatedContent);
    }
}
