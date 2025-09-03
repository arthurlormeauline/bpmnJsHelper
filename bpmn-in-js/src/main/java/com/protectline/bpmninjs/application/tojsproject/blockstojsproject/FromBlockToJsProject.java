package com.protectline.bpmninjs.application.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.common.block.BlockWriter;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;

import java.io.IOException;
import java.util.List;

public class FromBlockToJsProject {

    private final JsProject jsProject;
    private final FileUtil fileUtil;
    private final BlockWriter blockWriter;

    public FromBlockToJsProject(FileUtil fileUtil, BlockWriter blockWriter, MainFactory mainFactory) throws IOException {
        this.fileUtil = fileUtil;
        jsProject = new JsProject(fileUtil, mainFactory);
        this.blockWriter = blockWriter;
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        List<Block> blocks = blockWriter.readBlocksFromFile(fileUtil.getBlocksFile(process));
        jsProject.updateProject(process, blocks);
    }
}
