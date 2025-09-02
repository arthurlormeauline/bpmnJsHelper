package com.protectline.bpmninjs.application.tojsproject.blockstojsproject;

import com.protectline.bpmninjs.application.WriteBlock;
import com.protectline.bpmninjs.common.block.Block;
import com.protectline.bpmninjs.files.FileUtil;
import com.protectline.bpmninjs.jsproject.JsProject;
import com.protectline.bpmninjs.jsproject.TemplateProvider;
import com.protectline.bpmninjs.jsproject.blocksfactory.blockbuilder.BlockFromElementFactory;

import java.io.IOException;
import java.util.List;

public class FromBlockToJsProject {

    private final JsProject jsProject;
    private final FileUtil fileUtil;
    private final WriteBlock writeBlock;

    public FromBlockToJsProject(FileUtil fileUtil, WriteBlock writeBlock, TemplateProvider templateProvider) {
        this.fileUtil = fileUtil;
        jsProject = new JsProject(fileUtil, templateProvider, new BlockFromElementFactory(fileUtil));
        this.writeBlock = writeBlock;
    }

    public void updateJsProjectFromBlocks(String process) throws IOException {
        List<Block> blocks = writeBlock.readBlocksFromFile(fileUtil.getBlocksFile(process));
        jsProject.updateProject(process, blocks);
    }
}
