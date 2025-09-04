package com.protectline.bpmninjs.engine.tojsproject;

import com.protectline.bpmninjs.model.common.block.persist.BlockUtil;
import com.protectline.bpmninjs.engine.mainfactory.MainFactory;
import com.protectline.bpmninjs.engine.tojsproject.blockstojsproject.FromBlockToJsProject;
import com.protectline.bpmninjs.engine.tojsproject.bpmntoblocks.FromBpmnToBlocks;
import com.protectline.bpmninjs.engine.files.FileUtil;

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
