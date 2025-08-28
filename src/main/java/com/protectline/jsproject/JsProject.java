package com.protectline.jsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
        jsProjectUpdater.updateProject(process, blocks);
    }

    public List<Block> updateBlocks() {
        return null;
    }
}
