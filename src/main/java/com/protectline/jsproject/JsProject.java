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
        // 1 : verify that js project directory exist ; if so, delete it (cf fileUtil.getJsProjectDirectory())
        // 2 : copy from template the new js project
        // write to js project following blocks
    }

    public List<Block> updateBlocks() {
        return null;
    }
}
