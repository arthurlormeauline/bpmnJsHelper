package com.protectline.jsproject;

import com.protectline.common.block.Block;
import com.protectline.files.FileUtil;

import java.util.List;

public class JsProject {

    private final FileUtil fileUtil;


    public JsProject(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void updateProject(String process, List<Block> blocks) {
        // todo
    }

    public List<Block> updateBlocks() {
        return null;
    }
}
