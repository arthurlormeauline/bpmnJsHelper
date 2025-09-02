package com.protectline.bpmninjs.application.tojsproject;

import com.protectline.bpmninjs.common.block.persist.BlockUtil;
import com.protectline.bpmninjs.application.mainfactory.MainFactory;
import com.protectline.bpmninjs.application.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.bpmninjs.application.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.bpmninjs.application.files.FileUtil;

import java.io.IOException;

public class BpmnToJS {

    private final FromBpmnToBlocks bpmnToBlock;
    private final FromBlockToJsProject blockToJs;

    public BpmnToJS(FileUtil fileUtil, MainFactory mainFactory) throws IOException {
        bpmnToBlock = new FromBpmnToBlocks(fileUtil, mainFactory.getBlockBuilders(), new BlockUtil());
        blockToJs = new FromBlockToJsProject(fileUtil, new BlockUtil(), mainFactory);
    }

    public void createProject(String process) throws IOException {
        bpmnToBlock.createBlocksFromBpmn(process);
        blockToJs.updateJsProjectFromBlocks(process);
    }
}
