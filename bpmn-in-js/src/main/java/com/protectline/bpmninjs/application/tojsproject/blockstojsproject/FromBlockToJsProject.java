package com.protectline.bpmninjs.application.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;

import java.io.IOException;
import java.util.List;

import static com.protectline.bpmninjs.common.block.jsonblock.FunctionJsonBlockUtil.readBlocksFromFile;

public class FromBlockToJsProject {

    private final JsProject jsProject;
    private final FileUtil fileUtil;

    public FromBlockToJsProject(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        jsProject = new JsProject(fileUtil);
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        List<Block> blocks = readBlocksFromFile(fileUtil.getBlocksFile(process));
        jsProject.updateProject(process, blocks);
    }
}
