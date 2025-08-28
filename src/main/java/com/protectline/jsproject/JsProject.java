package com.protectline.jsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;

import java.io.IOException;
import java.util.List;

import static com.protectline.jsproject.jsupdater.JsUpdaterFactory.getJsUpdaters;
import static com.protectline.jsproject.updatertemplate.JsUpdaterTemplateUtil.readJsUpdaterTemplatesFromFile;

public class JsProject {

    private final FileUtil fileUtil;
    private final JsProjectUpdater jsProjectUpdater;

    public JsProject(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        jsProjectUpdater = new JsProjectUpdater(fileUtil);
    }

    public void updateProject(String process, List<Block> blocks) throws IOException {
        fileUtil.deleteJsDirectoryIfExists(process);
        fileUtil.copyTemplateToJsDirectory(process);
        var jsUpdaterTemplates = readJsUpdaterTemplatesFromFile(fileUtil);
        var updaters = getJsUpdaters(blocks, jsUpdaterTemplates);
        jsProjectUpdater.updateProject(process, blocks, updaters);
    }

    public List<Block> updateBlocks() {
        // todo
        return List.of();
    }
}
