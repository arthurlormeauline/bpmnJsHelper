package com.protectline.application.tojsproject.blockstojsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;
import com.protectline.jsproject.JsProject;

import java.io.IOException;
import java.util.List;


public class FromBlockToJsProject {

    private final JsProject jsProject;
    private final FileUtil fileUtil;

    public FromBlockToJsProject(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        jsProject = new JsProject(fileUtil);
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        // get blocks from file
        List<Block> blocks = null;
        jsProject.updateProject(process, blocks);
    }
}
